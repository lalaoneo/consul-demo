## consul原理机制

- demo使用
    * 修改properties文件的consul地址，然后执行mvn clean install
    * spring boot 版本号2.1.1
    * consul service版本号v1.4.0
    * provider可以启动多个,注意需要修改配置文件的端口号及健康检查URL的端口号

- 公共部分
    - springFactoryLoader扫描spring.factories文件

- consul基础
    - KEY/VALUE 存储
    - 是否持久化
        ```
        CLIENT表示consul的client模式，就是客户端模式。是consul节点的一种模式，这种模式下，所有注册到当前节点的服务会被转发到SERVER，本身是不持久化这些信息
        SERVER表示consul的server模式，表明这个consul是个server，这种模式下，功能和CLIENT都一样，唯一不同的是，它会把所有的信息持久化的本地，这样遇到故障，信息是可以被保留的
        ```
    - 数据一致性模式(client会把所有请求交给leader处理)
        - default
        ```
        Raft用到了leader约期的概念，意思是，在一个时间窗口中，leader认为自己的角色是稳定的。但是，如果leader节点与别的节点被分隔，即发生所谓“脑裂”现象，那么会有一个新的leader节点被选举出来。
        旧的leader节点将不能提交任何新的log entry， 但是如果它提供了对数据的读取，那么客户端读到的这些值可能是过期的。
        默认模式是基于leader约期的，因此客户端可能读到过期的值。但是这种模式是对读取速度和一致性的一种取舍，牺牲了某些情况下的强一致性，以换取更高的读取速度
        ```
        - consistent
        ```
        这种模式是强一致性模式。这种模式要求leader节点每次做读和写操作时都要与法定个数的节点去确认自己仍然是leader。 
        牺牲读的速度，保障了强一致性。
        ```
        - stale
        ```
        这种模式允许任何Consul server向外部提供读取操作，无论它是不是leader节点。 
        这种模式特别快，但是读到过期数据的可能性非常大。这种模式允许在没有leader节点的情况下对读请求做出相应，尽管实际上这时候Raft集群已经是处于不可用状态了。
        ```
    - leader选举及log entity日志同步通过raft算法;http://thesecretlivesofdata.com/raft/
    - Gossip
        ```
        Consul使用两个不同的gossip池。我们分别称为LAN和WAN池。每个数据中心有一个LAN gossip池，它包含数据中心的所有成员——client和server。LAN池用于几个目的：成员关系运行client自动发现server，
        减少配置量；分布式的故障检测允许故障检测的工作由整个集群承担，而不是集中在少数server上；最后gossip池允许可靠和快速的事件广播，比如leader选举。
        WAN池是全局唯一的，以为所有的server都应该加入WAN池，不论是哪个数据中心的。WAN池提供的成员关系允许server执行跨数据中心请求。集成的故障检测允许Consul优雅的处理整个数据中心失联，
        或者远程数据中心只有一个server。所有这些特征通过Serf提供。它被用来作为一个嵌入式的包来提供这些特征。从用户的角度，这是不重要的，因为这些抽象应该被Consul屏蔽。
        然而这对于开发者理解这个包是如何应用是很有用的。
        ```
    - 良好支持多活,一般配置为两地三中心

- consul底层通信协议
    - 通过http协议,consul暴露了很多标准的REST接口
    
- ConsulClient机制
    - 初试化ConsulAutoConfiguration
       
       --> 创建ConsulRawClient对象;其属性DefaultHttpTransport持有默认的HttpClient对象,用于http请求,初始化5个连接;keepAlive=true
       
       --> 初始化ConsulClient,其持有了各种功能的client,比如:agentClient(用于服务注册),HealthClient(用于健康检查)等;
       底层都是用ConsulRawClient去调用consul的REST接口;
       
- 服务注册
    - 注册流程:
    ```
    -->初始化ConsulAutoServiceRegistrationAutoConfiguration的ConsulRegistrationCustomizer,ConsulAutoRegistration
     
    -->ConsulServiceRegistryAutoConfiguration的ConsulServiceRegistry
     
    -->ServiceRegistryEndpointConfiguration的ServiceRegistryEndpoint
     
    -->ConsulAutoServiceRegistrationAutoConfiguration的ConsulAutoServiceRegistration
     
    -->程序启动完成通知监听类AbstractDiscoveryLifecycle
     
    -->调用子类ConsulAutoServiceRegistration的register
     
    -->调用ConsulServiceRegistry的register
     
    -->通过agentClient调用consul接口:/v1/agent/service/register注册
    ```
    
    - 注册时的数据结构:{"ID":"billfin-8080","Name":"billfin","Tags":[],"Address":"liziyi.fmscm.com","Port":8080,
    
      "Check":{"Interval":"10s","HTTP":"http://liziyi.fmscm.com:8080/health"}}
      
    - ID覆盖问题:一个程序多个实例如果直接注册到consul service上,后注册的会覆盖前面的;
      
      可以通过配置spring.cloud.consul.discovery.instanceId设置不同的实例ID;没有配置,
      
      系统默认取ApplicationContext.getId结果是:applicationName:port(比如:billFill:8080);
      
      consul推荐通过consul client来注册服务,client把数据结构发给service后,ID会重新生成
      
      ```
      [{
      	"ID": "3207d303-decb-a213-c3b3-056268a70018",
      	"Node": "fmeye-zookeeper-1",
      	"Address": "10.41.156.44",
      	"Datacenter": "dc1",
      	"TaggedAddresses": {
      		"lan": "10.41.156.44",
      		"wan": "10.41.156.44"
      	},
      	"NodeMeta": {
      		"consul-network-segment": ""
      	},
      	"ServiceID": "zookeeper",
      	"ServiceName": "zookeeper",
      	"ServiceTags": [],
      	"ServiceAddress": "",
      	"ServicePort": 2181,
      	"ServiceEnableTagOverride": false,
      	"CreateIndex": 34717,
      	"ModifyIndex": 34717
      }, {
      	"ID": "eb9c7b9b-2638-b49b-2bf5-3ccb0d704f51",
      	"Node": "fmeye-zookeeper-2",
      	"Address": "10.41.90.62",
      	"Datacenter": "dc1",
      	"TaggedAddresses": {
      		"lan": "10.41.90.62",
      		"wan": "10.41.90.62"
      	},
      	"NodeMeta": {
      		"consul-network-segment": ""
      	},
      	"ServiceID": "zookeeper",
      	"ServiceName": "zookeeper",
      	"ServiceTags": [],
      	"ServiceAddress": "",
      	"ServicePort": 2181,
      	"ServiceEnableTagOverride": false,
      	"CreateIndex": 34725,
      	"ModifyIndex": 34725
      }]
      ```
    
    - ConsulRegistrationCustomizer作用:获取ServletContext的contextPath,并加入到ConsulAutoRegistration
      
      的NewService的tags列表中;
    
    - ServiceRegistryEndpoint作用:暴露了两个REST接口/service-registry/instance-status(POST)设置状态;
                                                                                                       
       /service-registry/instance-status(GET)获取状态.
    
    - 健康检查机制:"Check":{"Interval":"10s","HTTP":"http://liziyi.fmscm.com:8080/health"}
       
       ```
       -->程序启动完成通知监听类AbstractDiscoveryLifecycle
         
       -->调用子类ConsulAutoServiceRegistration的setConfiguredPort
        
       -->调用ConsulAutoRegistration的initializePort构建NewService.Check对象
        
       -->由consul服务端定期调用此接口,检查程序健康性
       
       ```
- ConsulDiscoveryClient初试化
    - 初始化ConsulDiscoveryClientConfiguration
    ```
    -->初始化ConsulDiscoveryProperties获取consul地址及一些配置信息
     
    -->c初始化LifecycleRegistrationResolver,用于获取实例ID,端口号及加载bean;
     
    -->初始化ConsulDiscoveryClient,通过它可以获取服务列表,服务端发起的/health方法会最终调用到ConsulDiscoveryClient.getServices方法;
     
    -->初始化ConsulCatalogWatch,它会启动一个30秒的定时任务,定时刷新服务列表,并发布HeartbeatEvent心跳事件,
       使用zuul网关的时候有用,网关可以定时更新服务配置;
    ```

- 服务发现
    - ribbon配置初始化
    ```
    --> @RibbonClients(defaultConfiguration = ConsulRibbonClientConfiguration.class)为ribbon
       设置配置,集成Consul;
       
    --> 创建SpringClientFactory用于加载fegin,ribbon的bean
     
    --> 创建负载均衡客户端LoadBalancerClient:RibbonLoadBalancerClient,这个是可以单独使用的,@LoadBalanced开启客户端负载均衡
        因为使用feginClient,所以不再使用RibbonLoadBalancerClient进行操作;
     
    --> 创建重试代理工厂LoadBalancedRetryPolicyFactory:NeverRetryFactory,因为没有配置RetryTemplate,所以创建了不重试的工厂类;
     
    --> 创建配置文件工厂PropertiesFactory,缓存一些class的指定名称,方便后面直接进行获取;
     
    --> 如果ribbon.eager-load.enabled=true会进行饥饿加载指定的client,否则不会加载,要在第一次执行fegin请求时初始化配置,并加载;
        
        1: 初始化RibbonApplicationContextInitializer并监听ApplicationReadyEvent事件
         
        2: 调用initialize()加载指定clientName的bean,比如bean名称:provider-1,并不存在此bean,会经过转换加载ribbon配置并拉取指定服务列表;
    ```
    - ribbon懒惰加载
    ```
    --> ribbon默认方式,第一次调用才会初始化配置,并从consul上拉取服务列表,如果hystrix配置的超时时间为1秒,一般第一次调用会超时;
     
    --> LoadBalancerFeignClient执行请求时先获取IClientConfig,发现没有,初始化进行下面的配置
     
    --> 初始化客户端配置IClientConfig:DefaultClientConfigImpl,加载一下像默认的连接超时,读超时,serviceId等配置;
     
    --> 初始化服务器请求接口ServerList的实现方式:ConsulServerList,用于加载指定serviceId的consul服务列表
     
    --> 初始化服务列表过滤器ServerListFilter:HealthServiceServerListFilter,过滤掉那些健康检查未通过的服务实例;
     
    --> 初始化健康检查机制IPing:ConsulPing,调用consulServer的isPassingChecks()检查服务是否可用;
     
    --> 初始化ServerIntrospector:DefaultServerIntrospector
        RibbonLoadBalancerClient在执行请求或者选择服务时,会调用其检测端口的安全性,默认设置了443,8443安全端口,其他非安全端口需要使用https请求;
     
    --> 初始化路由规则IRule:ZoneAvoidanceRule,路由规则见下面的所有规则原理;
     
    --> 初始化服务更新者ServerListUpdater:PollingServerListUpdater,用于定时加载服务列表
     
    --> 初始化负载均衡器ILoadBalancer:ZoneAwareLoadBalancer,并初始化其父类DynamicServerListLoadBalancer,并加载所有服务列表;
        同事初始化基类BaseLoadBalancer,启动一个默认30秒的定时器,调用ConsulPing检查服务是否可用;
     
    --> PollingServerListUpdater.start()启动一个30秒更新服务列表的定时器;
     
    --> 初始化重试处理器RetryHandler:DefaultLoadBalancerRetryHandler,设置默认重试策略,相同服务默认重试0次,失败重试下一个服务1次;
        因为本项目没有设置重试,所以不重试;
     
    --> 初始化负载均衡上下文RibbonLoadBalancerContext:RibbonLoadBalancerContext,保存了负载均衡器ILoadBalancer,
        客户端配置信息IClientConfig以及重试机制RetryHandler;
    ```
    - ribbon负责均衡
        - RoundRobinRule机制:
            - 最多循环10次获取服务,如果未获取到返回null
             
            - 设置一个原子变量,与所有服务列表长度进行取模轮询所有服务列表;
            
        - RandomRule机制
            - 获取所有服务列表,并根据其长度为参数获取0到长度的随机值;
             
            - 获取可用服务列表,并根据索引获取服务;
             
            - 如果服务为空,会一直循环获取服务;
        - ZoneAvoidanceRule机制(ribbon默认路由规则)
            - 定义了一个属性CompositePredicate,组合了两个过滤规则;ZoneAvoidancePredicate,AvailabilityPredicate
             
            - ZoneAvoidancePredicate,AvailabilityPredicate两个为主过滤条件,AvailabilityPredicate同时又是次过滤条件;
             
            - AbstractServerPredicate为最次过滤条件,永远返回true;
             
            - ZoneAvoidancePredicate过滤逻辑:
            ```
            --> 主要是进行区域过滤,选择最近区域的服务列表;
             
            --> 首先需要配置niws.loadbalancer.zoneAvoidanceRule.enabled=true才能启用此过滤规则,默认为true;
             
            --> 默认服务Service.zone=UNKONOW,不使用区域过滤规则;
            ```
            - AvailabilityPredicate过滤逻辑
            ```
            --> niws.loadbalancer.availabilityFilteringRule.filterCircuitTripped短路由是否打开,默认没有打开所以不使用此过滤逻辑;
             
            --> 如果打开,判断断路由是否生效已断开,或者连接数是否大于最大连接数,是的,应该跳过此server;
            ```
            --> 通过上面两个过滤组件过滤出合适的服务列表;
             
            --> 再通过AbstractServerPredicate.incrementAndGetModulo()方法取模运算,轮询服务列表;
        - AvailabilityFilteringRule过滤逻辑
        ```
        --> 首先使用轮询策略选择出一个server
         
        --> 再使用AvailabilityPredicate进行过滤判断,逻辑见上面;如果不合适,再轮询下一个进行过滤;
         
        --> 轮询10次还是失败,交由PredicateBasedRule进行选择;因为组织了两个过滤策略,第一个由AvailabilityPredicate过滤,第二个始终返回true;
        ```
        - BestAvailableRule过滤逻辑
        ```
        --> 继承了ClientConfigEnabledRoundRobinRule,所以可以使用轮询策略,如果loadBalancerStats为空,使用轮询策略;
         
        --> 判断是否断路由已生效并断开,如果没有判断是否达到最大连接数;否则选择此server;
         
        --> 如果没有合适server,通过父类进行轮询server;
        ```
        - RetryRule路由规则
        ```
        --> 持有轮询路由规则实例RoundRobinRule;首先轮询一个server;
         
        --> 如果此server不可用,并且在重试时间范围内,选择下一个server;
         
        --> 如果没有选中返回null;
        ```
        - WeightedResponseTimeRule
        ```
        --> 初始化时会计算所有服务的总响应时间;然后计算每个服务的权重,权重=总响应时间-当个服务响应时间;
            服务响应时间越长,权重越小;
             
        --> 同时启动一个30秒的定时器,定时更新服务的权重;
         
        --> 如果所有服务的响应时间还没统计出来;通过父类RoundRobinRule路由规则选server,
         
        --> 通过random选择一个随机double值*最大权重重量,循环权重重量找出第一个大于等于权重重量的索引;选择此索引的server;
        ```
    - LoadBalancerFeignClient
        ```
        --> 初始化FeignContext,设置了配置文件列表;
         
        --> 如果feign.hystrix.enabled=true熔断机制打开,创建Feign.Builder:HystrixFeign.Builder,否则Feign.Builder;
            这个决定了使用哪个拦截器;如果打开使用HystrixInvocationHandler和SynchronousMethodHandler拦截器,否则使用SynchronousMethodHandler拦截器;
         
        --> 初始化Request.Options,设置超时配置;
         
        --> 初始化CachingSpringLoadBalancerFactory,持有SpringClientFactory对象,可以获取ribbon的IClientConfig,ILoadBalancer等;
         
        --> 初始化FeignRibbonClientAutoConfiguration,导入HttpClientFeignLoadBalancedConfiguration,因为没有此包,不导入;
         
        --> 导入OkHttpFeignLoadBalancedConfiguration,因为没有此包,不导入;
         
        --> 导入DefaultFeignLoadBalancedConfiguration,初始化LoadBalancerFeignClient,持有CachingSpringLoadBalancerFactory,SpringClientFactory
            并初始化了一个默认的Client.Default类,持有HttpURLConnection,用来执行http请求;
             
        --> 初始化Targeter:HystrixTargeter,用于构建feginClient接口的代理实现类;
         
        --> 初始化HasFeatures,给featuresEndpoint使用,方便查看系统的新特性功能,接口:/actuator/features
        ```
    - hystrix的熔断及降级原理
        - HystrixInvocationHandler会创建一个HystrixCommand对象,重写run()及getFallback(),run()执行远程调用,
          getFallback执行降级;
          
        - 对父观察者设置一个子观察者,通过lift(HystrixObservableTimeoutOperator)设置子观察者,
          调用HystrixObservableTimeoutOperator.call()方法,创建TimerListener对象,通过HystrixTimer启动一个在超时时间延迟执行的Scheduled
          非常巧妙的设计;
          
        - 如果超时会调用fallBack方法,没有抛出timeout异常;默认超时时间1000ms,默认尝试5次,5次都失败会熔断既断路器打开,5秒之后再尝试超时的服务既断路器半打开;如果还是失败继续熔断既断路器打开;
          否则断路器关闭,正常调用;
          
        - 默认配置:https://blog.csdn.net/hry2015/article/details/78554846
         
        - hystrix原理参考文献:https://www.jianshu.com/p/60074fe1bd86
         
        - hystrix强大容错机制另外一个机制隔离机制,默认是线程池隔离;为不同类型的请求创建线程池;不影响其他类型请求的调用;
          还有信号量机制:设置一个原子变量,设置一个阈值,默认20个,达到阈值进行降级处理,不同类型的请求互不影响;
          hystrix是在调用方进行配置的;既在调用方已经进行了隔离;
          参考文献:https://blog.csdn.net/dengqiang123456/article/details/75935122
    
- feginClient执行流程
    - FeignClientFactoryBean获取Client:LoadBalancerFeignClient及Targeter:HystrixTargeter对象;
     
    - 通过HystrixTargeter.target()构建代理实现类HardCodedTarget并注入SynchronousMethodHandler拦截器;
     
    - 执行feginClient接口的方法,会被SynchronousMethodHandler拦截;
     
    - 调用LoadBalancerFeignClient的execute方法;
     
    - 获取ribbon的IClientConfig:DefaultClientConfigImpl,并通过其属性CachingSpringLoadBalancerFactory获取
       ILoadBalancer:DynamicServerListLoadBalancer,并构建到FeignLoadBalancer中;
        
    - 调用FeignLoadBalancer的父类AbstractLoadBalancerAwareClient的executeWithLoadBalancer方法;
       构建LoadBalancerCommand类执行请求;
       
    - 调用ILoadBalancer的chooseServer,最终调用路由规则策略,选择server,然后调用Client.Default里面的HttpURLConnection执行请求;
    