<#macro form id action method="post" orientation="horizontal">
<form class="form-${orientation}" id="${id}" method="${method}" action="${action}">
    <#nested/>
</form>
</#macro>
<#macro fieldset legend class="" id="" icon="" collapsible=false>
<fieldset class="${class} ui-helper-clearfix" <#if id!="">id="${id}"</#if>>
    <legend class="ui-helper-clearfix">
        <#if icon!=""><i class="${icon}"> </i> </#if>
        ${legend} 
        <#if collapsible == true>
            <a data-toggle="collapse" data-target="#${id}-container">show...</a>
        </#if>
    </legend>
    <#if collapsible == true>
        <div id="${id}-container" class="collapse in">
            <#nested/>
        </div>
    <#else>
        <#nested/>
    </#if>
</fieldset>
</#macro>
<#macro actions>
<div class="form-actions ui-helper-clearfix" style="clear:both">
    <div class="left">
    <#nested/>
    </div>
    <div class="alert alert-success span6 ellipsis" style="display:none;"></div>
</div>
</#macro>
<#macro modal id title class="">
<div class="modal hide ${class}" id="${id}">
    <div class="modal-header">
        <button class="close" data-dismiss="modal">&times;</button>
        <h3>${title}</h3>
    </div>
    <#nested/>
</div>
</#macro>
<#macro modalform id title action="#" class="" orientation="horizontal" method="post">
<div class="modal hide ${class}" id="${id}">
    <div class="modal-header">
        <button class="close" data-dismiss="modal">&times;</button>
        <h3>${title}</h3>
    </div>
    <form class="form-${orientation}" method="${method}" action="${action}">
        <#nested/>
    </form>
</div>
</#macro>
<#macro modalbody>
<div class="modal-body">
    <#nested/>
</div>
</#macro>
<#macro modalfooter>
<div class="modal-footer">
    <div class="left">
        <#nested/>
    </div>
    <div class="alert alert-success span4 ellipsis" style="display:none;"></div>
</div>
</#macro>
<#macro action label id="" name="" class="" tooltip=""> 
    <button class="btn ${class}" <#if id!="">id="${id}" </#if><#if name!="">name="${name}"</#if> <#if tooltip!="">rel="tooltip" title="${tooltip}"</#if>>${label}</button>
</#macro>
<#macro cancel label="Cancel" class="" > 
    <button type="button" class="btn ${class} cancel">${label}</button>
</#macro>

<#macro submit label> 
    <button type="submit" class="btn btn-primary">${label}</button>
</#macro>

<#macro secondary>
</#macro>

<#macro text label name id="" value="" required=false class="" readonly=false>
<div class="control-group ${name?replace('.', '-')}_wrapper">
    <label class="control-label" <#if id!="">for="${id}"</#if> <#if name!="">name="${name}"</#if>>${label}</label>
    <div class="controls">
        <input type="text" 
               class="input-xlarge ${class}" 
               <#if id!="">id="${id}"</#if> 
               <#if name!="">name="${name}"</#if> 
               <#if value!="">value="${value}"</#if> 
               <#if readonly==true> readonly="readonly"</#if>>
        </input>
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

<#macro button label id text class="">
<div class="control-group ui-helper-clearfix">
    <label class="control-label" for="${id}"><button id="${id}" class="btn ${class}">${text}</button></label>
    <div class="controls">
        ${label}
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

<#macro custom label="" wrapper="" class="">
<div class="control-group ui-helper-clearfix<#if wrapper!=""> ${wrapper}_wrapper</#if>">
    <label class="control-label" >${label}</label>
    <div class="controls ${class}">
        <#nested/>
    </div>
</div>
</#macro>

<#macro constant label value name="" href="" default="" class="" id="">
<div class="control-group ui-helper-clearfix" <#if name!="">class="${name?replace('.', '-')}_wrapper"</#if>>
    <label class="control-label">${label}</label>
    <div class="controls">
        <#if name!="">
            <input type="text" name="${name}" <#if id!=""> id="${id}" </#if>value="${value}" <#if class!="">class="${class}"</#if> readonly="readonly"></input>
        </#if>
        <#if value="" && default!="">
            <#local value=default/>
        </#if>
    </div>
</div>
</#macro>

<#macro fileupload label id="" name="" action="" inputname="filename">
<div class='control-group ui-helper-clearfix form-inline'>
    <label class='control-label' <#if id!=''>for='${id}'</#if> <#if name!=''>name='${name}'</#if> >${label}</label>
    <div class="controls" <#if id!="">id="${id}"</#if> >
        <form enctype='multipart/form-data' method='post' action='${action}'>
            <input type='file' class='hide' name="${inputname}" id="${inputname}"></input>
            <div class='input-append left'>
                <span class='uneditable-input input-xlarge span6' name='filename'></span>
                <button name='select' class='btn '><i class='icon-comment'> </i> </button> 
                <button name='attach' class='btn '><i class='icon-upload'> </i> </button> 
                <button name='abort'  class='btn '><i class='icon-remove'> </i> </button> 
            </div>
            <div class='left'>
                <div class='progress active span3 clear hide'>
                    <div class='bar' style='width: 0%;"'></div>
                </div>
                <div class='alert span4 hide'></div>
            </div>
        </form>
    </div>
</div>
</#macro>

<#macro field id label checked=false>
<input type="radio" id="${id}" name="${fieldsetid}" <#if checked>checked="checked" style="clear:none;"</#if>/><label for="${id}" style="clear:none;">${label}</label>
</#macro>


