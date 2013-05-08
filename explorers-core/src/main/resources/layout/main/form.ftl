<!-- Wrapper for a form -->
<#macro form id action method="post" orientation="horizontal">
<form class="form-${orientation}" id="${id}" method="${method}" action="${action}">
    <#nested/>
</form>
</#macro>

<!-- Group of fields within a form -->
<#macro fieldset legend class="" id="">
<fieldset class="${class}" <#if id!="">id="${id}"</#if>>
    <legend>${legend}</legend>
    <#nested/>
</fieldset>
</#macro>

<!-- Actions for a form -->
<#macro actions>
<div class="form-actions ui-helper-clearfix" style="clear:both">
    <div class="left">
    <#nested/>
    </div>
    <div class="alert alert-success span6 ellipsis" style="display:none;"></div>
</div>
</#macro>

<!-- Modal dialog wraping a form -->
<#macro modal id title class="">
<div class="modal hide ${class}" id="${id}">
    <div class="modal-header">
        <button class="close" data-dismiss="modal">&times;</button>
        <h3>${title}</h3>
    </div>
    <div class="modal-body">
        <#nested/>
   </div>
</div>
</#macro>

<#macro action label id="" name="" class=""> 
    <button class="btn ${class}" <#if id!="">id="${id}" </#if><#if name!="">name="${name}"</#if>>${label}</button>
</#macro>

<#macro submit label> 
    <button type="submit" class="btn btn-primary">${label}</button>
</#macro>

<#macro secondary>
</#macro>

<#macro text label name id="" value="" required=false class="">
<div class="control-group ${name?replace('.', '-')}_wrapper">
    <label class="control-label" <#if id!="">for="${id}"</#if> <#if name!="">name="${name}"</#if>>${label}</label>
    <div class="controls">
        <input type="text" class="input-xlarge ${class}" <#if id!="">id="${id}"</#if> <#if name!="">name="${name}"</#if> <#if value!="">value="${value}"</#if>></input>
        <#if required==true><span class="required-indicator">*</span></#if><#nested/>
    </div>
</div>
</#macro>

<#macro textarea label name id="" required=false>
<div class="control-group ${name?replace('.', '-')}_wrapper">
    <label class="control-label" <#if id!="">for="${id}"</#if> <#if name!="">name="${name}"</#if>>${label}</label>
    <div class="controls"><textarea <#if id!="">id="${id}" </#if> <#if name!="">name="${name}"</#if>><#nested/></textarea><#if required==true><span class="required-indicator">*</span></#if>
    </div>
</div>
</#macro>

<#macro select label name id="">
<div class="control-group ${name?replace('.', '-')}_wrapper">
    <label class="control-label" for="${name}">${label}</label>
    <div class="controls">
        <select <#if id!="">id="${id}" </#if>name="${name}">
            <#nested/>
        </select>
    </div>
</div>
</#macro> 

<#macro checkbox label name id="" checked=true value=true>
<div class="control-group ${name?replace('.', '-')}_wrapper">
    <label class="control-label" for="${name}">${label}</label>
    <div class="controls">
        <input type="checkbox" <#if id!="">id="${id}" </#if>name="${name}" value="${value?string}" checked="${checked?string}"></input>
    </div>
</div>
</#macro>

<#macro radio_group label id="">
<div class="control-group <#if id!="">${id}_wrapper</#if>">
    <label class="control-label" >${label}</label>
    <div class="controls">
        <div class="radio-group-horizontal ui-helper-clearfix" id="${id}">
            <#nested/>
        </div>
    </div>
</div>
</#macro>

<#macro radio label name id value checked=false>
<input type="radio" id="${id}" value="${value}" name="${name}" <#if checked>checked="checked"</#if> /><label for="${id}">${label}</label>
</#macro>

<#macro button label id text>
<div class="control-group ui-helper-clearfix">
    <label class="control-label" for="${id}">
    <div class="controls">
        <button id="${id}" type="button">${text}</button>${label}</label>
    </div>
</div>
</#macro>

<#macro hidden name value="" id="id">
<div class="${name?replace('.', '-')}_wrapper" style="display:none;">
    <label class="control-label" ></label>
    <div class="controls">
        <input <#if id!="">id="${id}"</#if> type="hidden" name="${name}" value="${value}"></input>
    </div>
</div>
</#macro>

<#macro custom label="" wrapper="">
<div class="control-group ui-helper-clearfix<#if wrapper!=""> ${wrapper}_wrapper</#if>">
    <label class="control-label" >${label}</label>
    <div class="controls">
        <#nested/>
    </div>
</div>
</#macro>

<#macro constant label value name="" href="" default="" class="">
<div class="control-group ui-helper-clearfix" <#if name!="">class="${name?replace('.', '-')}_wrapper"</#if>>
    <label class="control-label">${label}</label>
    <div class="controls">
        <#if name!="">
            <input type="text" name="${name}" value="${value}" <#if class!="">class="${class}"</#if> readonly="readonly"></input>
        </#if>
        <#if value="" && default!="">
            <#local value=default/>
        </#if>
    </div>
</div>
</#macro>

<#macro field id label checked=false>
<input type="radio" id="${id}" name="${fieldsetid}" <#if checked>checked="checked" style="clear:none;"</#if>/><label for="${id}" style="clear:none;">${label}</label>
</#macro>
