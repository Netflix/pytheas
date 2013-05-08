<#macro message level closable=true>
<div class="message-container ui-corner-all message-${level}">
    <div class="message-header"><#if closable == true><span class='ui-icon ui-icon-closethick'></span></#if></div>
    <div class="message-content"><#nested></div>
</div>
</#macro>

<#macro portlet title id="" refresh=false filter=false collapsible=false class="">
<div class="portlet ui-widget-content ui-corner-all ${class}" <#if id != "">id="${id}"</#if> >
    <div class="portlet-header ui-widget-header ui-helper-clearfix ui-corner-tr ui-corner-tl">

        <#if collapsible>
        <span class='ui-icon ui-icon-minusthick portlet-toggle' style="cursor:pointer;float:right;"></span>
        </#if>
        <#if refresh == true>
            <span class='ui-icon ui-icon-refresh' style="cursor:pointer;"></span>
        </#if>
        
        <#if filter == true>
            <input type='text' class="portlet-filter" placeholder="filter"></input>
        </#if>
        <span>${title}</span>
    </div>
    <div class="portlet-content ui-corner-br ui-corner-bl ui-helper-clearfix">
        <#nested/>
    </div>
</div>
</#macro>

<#macro toolbar_container>
</#macro>

<#macro toolbar_group>
</#macro>

<#macro crumb title="..." href="" id="">
<#if id !=""><li id="${id}"><#else><li></#if>
<#if href != ""><a href=${href}>${title}</a><#else>${title}</#if>
</li>
</#macro>

<#macro breadcrumbs>
<div class="breadCrumbHolder module">
    <div id="breadcrumbs" class="breadCrumb module">
        <ul>
        <#if RequestContext.contextPath=="">
            <@crumb "Home" "/"/>
        <#else>
            <@crumb "Home" "${RequestContext.contextPath}"/>
        </#if>
<@crumb "${Explorer.title}" "${RequestContext.pathToRoot}${Explorer.name}"/>
            <#nested>
        </ul>
    </div>
</div>
</#macro>
