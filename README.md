# consul-demo


## filebeat.yml 配置文件
```properties
filebeat.inputs:
- type: log
  paths:
    - /home/service/apollo-build-scripts-master/service/*.log
	
output.logstash:
  hosts: ["192.168.245.128:5044"]
  index: "filebeat-%{[beat.version]}-%{+yyyy.MM.dd}"

filebeat.config.modules:
  enabled: false
```

## 启动命令
* `docker run -it --name filebeat -v /home/service/docker/filebeat/conf/filebeat.yml:/filebeat.yml -v /home/service/apollo-build-scripts-master/service/apollo-service.log:/home/apollo-service.log:ro --net=host prima/filebeat:latest`

## 启动日志
```log
[root@localhost conf]# docker run -it --name filebeat -v /home/service/docker/filebeat/conf/filebeat.yml:/filebeat.yml -v /home/service/apollo-build-scripts-master/service/apollo-service.log:/home/apollo-service.log:ro --net=host prima/filebeat:latest
2019-01-29T03:24:37.886Z	INFO	instance/beat.go:468	Home path: [/] Config path: [/] Data path: [//data] Logs path: [//logs]
2019-01-29T03:24:37.886Z	INFO	instance/beat.go:475	Beat UUID: 98cd712b-0fb4-48f7-a560-6b79a9fdaed2
2019-01-29T03:24:37.886Z	INFO	instance/beat.go:213	Setup Beat: filebeat; Version: 6.2.3
2019-01-29T03:24:37.887Z	INFO	pipeline/module.go:76	Beat name: localhost.localdomain
2019-01-29T03:24:37.887Z	ERROR	fileset/modules.go:95	Not loading modules. Module directory not found: /module
2019-01-29T03:24:37.887Z	INFO	instance/beat.go:301	filebeat start running.
2019-01-29T03:24:37.887Z	INFO	registrar/registrar.go:71	No registry file found under: /data/registry. Creating a new registry file.
2019-01-29T03:24:37.890Z	INFO	[monitoring]	log/log.go:97	Starting metrics logging every 30s
2019-01-29T03:24:37.896Z	INFO	registrar/registrar.go:108	Loading registrar data from /data/registry
2019-01-29T03:24:37.896Z	INFO	registrar/registrar.go:119	States Loaded from registrar: 0
2019-01-29T03:24:37.896Z	WARN	beater/filebeat.go:261	Filebeat is unable to load the Ingest Node pipelines for the configured modules because the Elasticsearch output is not configured/enabled. If you have already loaded the Ingest Node pipelines or are using Logstash pipelines, you can ignore this warning.
2019-01-29T03:24:37.896Z	INFO	crawler/crawler.go:48	Loading Prospectors: 0
2019-01-29T03:24:37.896Z	INFO	crawler/crawler.go:82	Loading and starting Prospectors completed. Enabled prospectors: 0
2019-01-29T03:24:37.896Z	INFO	cfgfile/reload.go:127	Config reloader started
2019-01-29T03:24:37.897Z	INFO	cfgfile/reload.go:219	Loading of config files completed.
2019-01-29T03:25:07.897Z	INFO	[monitoring]	log/log.go:124	Non-zero metrics in the last 30s	{"monitoring": {"metrics": {"beat":{"cpu":{"system":{"ticks":20,"time":28},"total":{"ticks":40,"time":49,"value":40},"user":{"ticks":20,"time":21}},"info":{"ephemeral_id":"0b2cd273-53eb-47d3-8f61-660a4308ca58","uptime":{"ms":30015}},"memstats":{"gc_next":4473924,"memory_alloc":2702032,"memory_total":2702032,"rss":11272192}},"filebeat":{"harvester":{"open_files":0,"running":0}},"libbeat":{"config":{"module":{"running":0},"reloads":1},"output":{"type":"logstash"},"pipeline":{"clients":0,"events":{"active":0}}},"registrar":{"states":{"current":0},"writes":1},"system":{"cpu":{"cores":1},"load":{"1":0.01,"15":0.12,"5":0.09,"norm":{"1":0.01,"15":0.12,"5":0.09}}}}}}
2019-01-29T03:25:37.892Z	INFO	[monitoring]	log/log.go:124	Non-zero metrics in the last 30s	{"monitoring": {"metrics": {"beat":{"cpu":{"system":{"ticks":30,"time":30},"total":{"ticks":50,"time":51,"value":50},"user":{"ticks":20,"time":21}},"info":{"ephemeral_id":"0b2cd273-53eb-47d3-8f61-660a4308ca58","uptime":{"ms":60010}},"memstats":{"gc_next":4473924,"memory_alloc":2896720,"memory_total":2896720,"rss":233472}},"filebeat":{"harvester":{"open_files":0,"running":0}},"libbeat":{"config":{"module":{"running":0}},"pipeline":{"clients":0,"events":{"active":0}}},"registrar":{"states":{"current":0}},"system":{"load":{"1":0,"15":0.12,"5":0.08,"norm":{"1":0,"15":0.12,"5":0.08}}}}}}
2019-01-29T03:26:07.892Z	INFO	[monitoring]	log/log.go:124	Non-zero metrics in the last 30s	{"monitoring": {"metrics": {"beat":{"cpu":{"system":{"ticks":30,"time":30},"total":{"ticks":50,"time":52,"value":50},"user":{"ticks":20,"time":22}},"info":{"ephemeral_id":"0b2cd273-53eb-47d3-8f61-660a4308ca58","uptime":{"ms":90010}},"memstats":{"gc_next":4473924,"memory_alloc":3086064,"memory_total":3086064}},"filebeat":{"harvester":{"open_files":0,"running":0}},"libbeat":{"config":{"module":{"running":0}},"pipeline":{"clients":0,"events":{"active":0}}},"registrar":{"states":{"current":0}},"system":{"load":{"1":0,"15":0.12,"5":0.07,"norm":{"1":0,"15":0.12,"5":0.07}}}}}}

```
