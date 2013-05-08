<#macro breadcrumbs>
<ul class="breadcrumb">
    <#if contextPath=="">
        <li><a href="/"><i class="icon-home"></i></a><span class="divider">/</span></li>
    <#else>
        <li><a href="${RequestContext.contextPath}">Home</a><span class="divider">/</span></li>
    </#if>
    <li><a href="${RequestContext.pathToRoot}${Explorer.name}">${Explorer.title}</a><span class="divider">/</span></li>
    <#nested/>
</ul>
</#macro>

<#macro crumb href text>
<li>
    <a href="${href}">${text}</a><span class="divider">/</span>
</li>
</#macro>

<#macro crumb_active text>
<li class="active">${text}</li>
</#macro>

<#macro toolbar>
</#macro>

<#macro btngroup>
</#macro>

<#macro table id>
<div class="mini-layout">
<table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered" id="${id}" style="width:100%"></table>
</div>
</#macro>

<#macro portlet title id="" refresh=false filter=false collapsible=false class="">
<div class="mini-layout ${class}" <#if id != "">id="${id}"</#if> >
    <div class="portlet-header">

        <#if collapsible>
        <a class="close" data-dismiss="alert" href="#">&times;</a>
        </#if>
        
        <#if refresh == true>
            <span class='ui-icon ui-icon-refresh' style="cursor:pointer;"></span>
        </#if>
        
        <#if filter == true>
            <input type='text' class="portlet-filter" placeholder="filter"></input>
        </#if>
        <legend>${title}</legend>
    </div>
    <div class="portlet-content ui-corner-br ui-corner-bl ui-helper-clearfix">
        <#nested/>
    </div>
</div>
</#macro>

<#macro alert level="error">
<div class="alert alert-block alert-${level} fade in">
<button type="button" class="close" data-dismiss="alert"><i class="icon-remove"></i></button>
<#nested/>
</div>
</#macro>


