<div id="layoutheader" class="env-${Global.environmentName} ui-helper-clearfix" style="padding:3px;">
    <div style="float: left" id="layoutheader-main">
        <#if Explorers.explorers?size &gt; 1>
            <h1 style="cursor:pointer">${Global.applicationName} ${Global.applicationVersion}</h1>
            <#if !Global.isLocalExplorer>
                <select id="nf-regions">
                    <#list Global.crosslinks?keys as name>
                        <#assign dc=Global.crosslinks[name]/>
                        <option value="${dc.region}.${dc.env}" href="${dc.href}" region="${dc.region}" env="${dc.env}">${dc.title}</option>
                    </#list>
                </select>
            <#else>
                ${Global.currentRegion}.${Global.environmentName}
            </#if>
            </h1>
            
            <#if Explorer?exists>
                <select id="nf-modules">
                <#list Explorers.explorers as m>
                	<option value="${m.name}"><#if m.title?exists>${m.title}<#else>${m.name} (missing title)</#if></option>
                </#list>
                </select>
            </#if>
        <#else>
            <h1>Admin Resources: ${Global.applicationName} ${Global.applicationVersion} ${Global.currentRegion}.${Global.environmentName}</h1>
        </#if>
    </div>
    <div class="layoutheader-info right">
        <div><span id="uptime">${Global.startTime}</span></div>
        <div>
            <#if Global.environment.EC2_AVAILABILITY_ZONE??>
            <span>${Global.environment.EC2_AVAILABILITY_ZONE}</span>
            <#else>
            local
            </#if>
        </div>
    </div>
    <div style="float: right" class="layoutheader-info">
        <div>
            <#if Global.environment.EC2_INSTANCE_ID??>
            <span>${Global.environment.EC2_INSTANCE_ID}</span>
            <#elseif Global.environment.CDC_PREW2KHOST??>
            <span>${Global.environment.CDC_PREW2KHOST}</span>
            <#else>
            local
            </#if>
        </div>
        <div>
            <#if Global.environment.EC2_AMI_ID??>
            <span>${Global.environment.EC2_AMI_ID}</span>
            <#else>
            dev
            </#if>
        </div>
    </div>
</div>