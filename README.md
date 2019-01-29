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

## logstash日志及调试
```log
The stdin plugin is now waiting for input:
[2019-01-29T01:58:22,734][INFO ][logstash.agent           ] Pipelines running {:count=>1, :running_pipelines=>[:main], :non_running_pipelines=>[]}
[2019-01-29T01:58:23,556][INFO ][logstash.outputs.elasticsearch] Installing elasticsearch template to _template/logstash
[2019-01-29T01:58:24,602][WARN ][logstash.outputs.elasticsearch] You are using a deprecated config setting "document_type" set in elasticsearch. Deprecated settings will continue to work, but are scheduled for removal from logstash in the future. Document types are being deprecated in Elasticsearch 6.0, and removed entirely in 7.0. You should avoid this feature If you have any questions about this, please visit the #logstash channel on freenode irc. {:name=>"document_type", :plugin=><LogStash::Outputs::ElasticSearch bulk_path=>"/_xpack/monitoring/_bulk?system_id=logstash&system_api_version=2&interval=1s", hosts=>[http://192.168.245.128:9200], sniffing=>false, manage_template=>false, id=>"cf8fd9676fedad53db8c0ce3b0aed7230197584aefff9de59f1b13e53aee314f", document_type=>"%{[@metadata][document_type]}", enable_metric=>true, codec=><LogStash::Codecs::Plain id=>"plain_e5df2ea0-a36f-4a1b-8a21-0b977785bd81", enable_metric=>true, charset=>"UTF-8">, workers=>1, template_name=>"logstash", template_overwrite=>false, doc_as_upsert=>false, script_type=>"inline", script_lang=>"painless", script_var_name=>"event", scripted_upsert=>false, retry_initial_interval=>2, retry_max_interval=>64, retry_on_conflict=>1, action=>"index", ssl_certificate_verification=>true, sniffing_delay=>5, timeout=>60, pool_max=>1000, pool_max_per_route=>100, resurrect_delay=>5, validate_after_inactivity=>10000, http_compression=>false>}
[2019-01-29T01:58:24,634][INFO ][logstash.pipeline        ] Starting pipeline {:pipeline_id=>".monitoring-logstash", "pipeline.workers"=>1, "pipeline.batch.size"=>2, "pipeline.batch.delay"=>50}
[2019-01-29T01:58:24,725][INFO ][logstash.outputs.elasticsearch] Elasticsearch pool URLs updated {:changes=>{:removed=>[], :added=>[http://192.168.245.128:9200/]}}
[2019-01-29T01:58:24,786][WARN ][logstash.outputs.elasticsearch] Restored connection to ES instance {:url=>"http://192.168.245.128:9200/"}
[2019-01-29T01:58:24,796][INFO ][logstash.outputs.elasticsearch] ES Output version determined {:es_version=>6}
[2019-01-29T01:58:24,796][WARN ][logstash.outputs.elasticsearch] Detected a 6.x and above cluster: the `type` event field won't be used to determine the document _type {:es_version=>6}
[2019-01-29T01:58:24,807][INFO ][logstash.outputs.elasticsearch] New Elasticsearch output {:class=>"LogStash::Outputs::ElasticSearch", :hosts=>["http://192.168.245.128:9200"]}
[2019-01-29T01:58:24,918][INFO ][logstash.pipeline        ] Pipeline started successfully {:pipeline_id=>".monitoring-logstash", :thread=>"#<Thread:0x3c857145 run>"}
[2019-01-29T01:58:25,019][INFO ][logstash.agent           ] Pipelines running {:count=>2, :running_pipelines=>[:main, :".monitoring-logstash"], :non_running_pipelines=>[]}
[2019-01-29T01:58:25,752][INFO ][logstash.agent           ] Successfully started Logstash API endpoint {:port=>9600}
hello word
{
       "message" => "hello word",
      "@version" => "1",
          "host" => "c885b28ff247",
    "@timestamp" => 2019-01-29T01:59:17.679Z
}
hello word im^H am lori
{
       "message" => "hello word im\b am lori",
      "@version" => "1",
          "host" => "c885b28ff247",
    "@timestamp" => 2019-01-29T02:00:21.545Z
}
helo^Hl
{
       "message" => "helo\bl",
      "@version" => "1",
          "host" => "c885b28ff247",
    "@timestamp" => 2019-01-29T02:00:31.121Z
}
hello word i am lori
{
       "message" => "hello word i am lori",
      "@version" => "1",
          "host" => "c885b28ff247",
    "@timestamp" => 2019-01-29T02:00:35.783Z
}
i am lori
{
       "message" => "i am lori",
      "@version" => "1",
          "host" => "c885b28ff247",
    "@timestamp" => 2019-01-29T02:17:06.931Z
}
```
