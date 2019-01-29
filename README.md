# consul-demo


## logstash.conf 配置文件
```properties
input {
  beats {
    port => 5044
  }
  stdin {}
}

output {
  elasticsearch {
    hosts => ["http://192.168.245.128:9200"]
    #index => "%{[@metadata][beat]}-%{[@metadata][version]}-%{+YYYY.MM.dd}"
    #user => "elastic"
    #password => "changeme"
  }
  stdout {}
}

```
## logstash 日志
```log
{
       "message" => "2019-01-29 13:38:24.150  INFO 23888 --- [Eureka-EvictionTimer] c.n.e.registry.AbstractInstanceRegistry  : Running the evict task with compensationTime 1ms",
          "tags" => [
        [0] "beats_input_codec_plain_applied"
    ],
         "input" => {
        "type" => "log"
    },
      "@version" => "1",
          "beat" => {
         "version" => "6.5.4",
        "hostname" => "localhost.localdomain",
            "name" => "localhost.localdomain"
    },
        "offset" => 544550,
    "@timestamp" => 2019-01-29T05:38:31.088Z,
    "prospector" => {
        "type" => "log"
    },
        "source" => "/usr/share/filebeat/logs/apollo-service.log",
          "host" => {
        "name" => "localhost.localdomain"
    }
}
{
       "message" => "2019-01-29 13:38:26.167  INFO 23888 --- [AsyncResolver-bootstrap-executor-0] c.n.d.s.r.aws.ConfigClusterResolver      : Resolving eureka endpoints via configuration",
          "tags" => [
        [0] "beats_input_codec_plain_applied"
    ],
         "input" => {
        "type" => "log"
    },
      "@version" => "1",
          "beat" => {
         "version" => "6.5.4",
        "hostname" => "localhost.localdomain",
            "name" => "localhost.localdomain"
    },
        "offset" => 544705,
    "@timestamp" => 2019-01-29T05:38:31.088Z,
    "prospector" => {
        "type" => "log"
    },
          "host" => {
        "name" => "localhost.localdomain"
    },
        "source" => "/usr/share/filebeat/logs/apollo-service.log"
}

```

## filebeat 日志
```log
[root@localhost bin]# docker run -it --name filebeat -v /home/service/docker/filebeat/conf/filebeat.yml:/usr/share/filebeat/filebeat.yml -v /home/service/apollo-build-scripts-master/service/apollo-service.log:/usr/share/filebeat/logs/apollo-service.log:ro --net=host docker.elastic.co/beats/filebeat:6.5.4
2019-01-29T05:38:15.894Z	INFO	instance/beat.go:592	Home path: [/usr/share/filebeat] Config path: [/usr/share/filebeat] Data path: [/usr/share/filebeat/data] Logs path: [/usr/share/filebeat/logs]
2019-01-29T05:38:15.897Z	INFO	instance/beat.go:599	Beat UUID: c4a6482c-b58c-4d9f-a67b-75b5fef6755e
2019-01-29T05:38:15.897Z	INFO	[seccomp]	seccomp/seccomp.go:116	Syscall filter successfully installed
2019-01-29T05:38:15.897Z	INFO	[beat]	instance/beat.go:825	Beat info	{"system_info": {"beat": {"path": {"config": "/usr/share/filebeat", "data": "/usr/share/filebeat/data", "home": "/usr/share/filebeat", "logs": "/usr/share/filebeat/logs"}, "type": "filebeat", "uuid": "c4a6482c-b58c-4d9f-a67b-75b5fef6755e"}}}
2019-01-29T05:38:15.897Z	INFO	[beat]	instance/beat.go:834	Build info	{"system_info": {"build": {"commit": "bd8922f1c7e93d12b07e0b3f7d349e17107f7826", "libbeat": "6.5.4", "time": "2018-12-17T20:22:29.000Z", "version": "6.5.4"}}}
2019-01-29T05:38:15.897Z	INFO	[beat]	instance/beat.go:837	Go runtime info	{"system_info": {"go": {"os":"linux","arch":"amd64","max_procs":1,"version":"go1.10.6"}}}
2019-01-29T05:38:15.902Z	INFO	[beat]	instance/beat.go:841	Host info	{"system_info": {"host": {"architecture":"x86_64","boot_time":"2019-01-25T04:09:54Z","containerized":true,"name":"localhost.localdomain","ip":["127.0.0.1/8","::1/128","192.168.245.128/24","fe80::8b5:9547:c092:43ab/64","172.17.0.1/16","fe80::42:a9ff:fe9b:4a23/64","fe80::cc64:f1ff:fe17:5d3a/64","fe80::f82b:4aff:fea9:3063/64","fe80::2409:e9ff:fec9:646/64","fe80::cdd:cbff:fe77:2267/64","fe80::e0be:faff:fee1:c444/64"],"kernel_version":"3.10.0-957.1.3.el7.x86_64","mac":["00:0c:29:85:a9:e0","02:42:a9:9b:4a:23","ce:64:f1:17:5d:3a","fa:2b:4a:a9:30:63","26:09:e9:c9:06:46","0e:dd:cb:77:22:67","e2:be:fa:e1:c4:44"],"os":{"family":"redhat","platform":"centos","name":"CentOS Linux","version":"7 (Core)","major":7,"minor":6,"patch":1810,"codename":"Core"},"timezone":"UTC","timezone_offset_sec":0}}}
2019-01-29T05:38:15.902Z	INFO	[beat]	instance/beat.go:870	Process info	{"system_info": {"process": {"capabilities": {"inheritable":["chown","dac_override","fowner","fsetid","kill","setgid","setuid","setpcap","net_bind_service","net_raw","sys_chroot","mknod","audit_write","setfcap"],"permitted":null,"effective":null,"bounding":["chown","dac_override","fowner","fsetid","kill","setgid","setuid","setpcap","net_bind_service","net_raw","sys_chroot","mknod","audit_write","setfcap"],"ambient":null}, "cwd": "/usr/share/filebeat", "exe": "/usr/share/filebeat/filebeat", "name": "filebeat", "pid": 1, "ppid": 0, "seccomp": {"mode":"filter"}, "start_time": "2019-01-29T05:38:15.090Z"}}}
2019-01-29T05:38:15.903Z	INFO	instance/beat.go:278	Setup Beat: filebeat; Version: 6.5.4
2019-01-29T05:38:15.903Z	INFO	[publisher]	pipeline/module.go:110	Beat name: localhost.localdomain
2019-01-29T05:38:15.903Z	INFO	instance/beat.go:400	filebeat start running.
2019-01-29T05:38:15.903Z	INFO	registrar/registrar.go:97	No registry file found under: /usr/share/filebeat/data/registry. Creating a new registry file.
2019-01-29T05:38:15.904Z	INFO	[monitoring]	log/log.go:117	Starting metrics logging every 30s
2019-01-29T05:38:15.906Z	INFO	registrar/registrar.go:134	Loading registrar data from /usr/share/filebeat/data/registry
2019-01-29T05:38:15.906Z	INFO	registrar/registrar.go:141	States Loaded from registrar: 0
2019-01-29T05:38:15.906Z	WARN	beater/filebeat.go:374	Filebeat is unable to load the Ingest Node pipelines for the configured modules because the Elasticsearch output is not configured/enabled. If you have already loaded the Ingest Node pipelines or are using Logstash pipelines, you can ignore this warning.
2019-01-29T05:38:15.906Z	INFO	crawler/crawler.go:72	Loading Inputs: 1
2019-01-29T05:38:15.907Z	INFO	log/input.go:138	Configured paths: [/usr/share/filebeat/logs/apollo-service.log]
2019-01-29T05:38:15.907Z	INFO	input/input.go:114	Starting input of type: log; ID: 3815057039927849581 
2019-01-29T05:38:15.907Z	INFO	crawler/crawler.go:106	Loading and starting Inputs completed. Enabled inputs: 1
2019-01-29T05:38:15.907Z	INFO	log/harvester.go:254	Harvester started for file: /usr/share/filebeat/logs/apollo-service.log
2019-01-29T05:38:15.931Z	INFO	pipeline/output.go:95	Connecting to backoff(async(tcp://192.168.245.128:5044))
2019-01-29T05:38:15.949Z	INFO	pipeline/output.go:105	Connection to backoff(async(tcp://192.168.245.128:5044)) established
2019-01-29T05:38:45.928Z	INFO	[monitoring]	log/log.go:144	Non-zero metrics in the last 30s	{"monitoring": {"metrics": {"beat":{"cpu":{"system":{"ticks":200,"time":{"ms":204}},"total":{"ticks":290,"time":{"ms":298},"value":290},"user":{"ticks":90,"time":{"ms":94}}},"handles":{"limit":{"hard":65536,"soft":65536},"open":7},"info":{"ephemeral_id":"204c67fc-5f4f-43a5-bf75-2b2e418f4b27","uptime":{"ms":30031}},"memstats":{"gc_next":7210976,"memory_alloc":5288736,"memory_total":23980560,"rss":16326656}},"filebeat":{"events":{"added":3343,"done":3343},"harvester":{"open_files":1,"running":1,"started":1}},"libbeat":{"config":{"module":{"running":0}},"output":{"events":{"acked":3342,"batches":5,"total":3342},"read":{"bytes":42},"type":"logstash","write":{"bytes":104030}},"pipeline":{"clients":1,"events":{"active":0,"filtered":1,"published":3342,"retry":2048,"total":3343},"queue":{"acked":3342}}},"registrar":{"states":{"current":1,"update":3343},"writes":{"success":6,"total":6}},"system":{"cpu":{"cores":1},"load":{"1":1.86,"15":0.37,"5":0.88,"norm":{"1":1.86,"15":0.37,"5":0.88}}}}}}
2019-01-29T05:39:15.910Z	INFO	[monitoring]	log/log.go:144	Non-zero metrics in the last 30s	{"monitoring": {"metrics": {"beat":{"cpu":{"system":{"ticks":220,"time":{"ms":19}},"total":{"ticks":320,"time":{"ms":25},"value":320},"user":{"ticks":100,"time":{"ms":6}}},"handles":{"limit":{"hard":65536,"soft":65536},"open":7},"info":{"ephemeral_id":"204c67fc-5f4f-43a5-bf75-2b2e418f4b27","uptime":{"ms":60021}},"memstats":{"gc_next":7497728,"memory_alloc":3779224,"memory_total":25823936,"rss":483328}},"filebeat":{"events":{"added":1,"done":1},"harvester":{"open_files":1,"running":1}},"libbeat":{"config":{"module":{"running":0}},"output":{"events":{"acked":1,"batches":1,"total":1},"read":{"bytes":6},"write":{"bytes":361}},"pipeline":{"clients":1,"events":{"active":0,"published":1,"total":1},"queue":{"acked":1}}},"registrar":{"states":{"current":1,"update":1},"writes":{"success":1,"total":1}},"system":{"load":{"1":1.29,"15":0.36,"5":0.83,"norm":{"1":1.29,"15":0.36,"5":0.83}}}}}}

```
