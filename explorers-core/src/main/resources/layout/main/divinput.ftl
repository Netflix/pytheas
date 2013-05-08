<#macro section id title="">
<div class="section-container ui-corner-all" id="${id}">
<div class="section-title">${title}</div>
<div class="section-contents"><#nested/></div>
<div class="section-footer"></div>
</div>
</#macro>

<#macro form id action method="post">
<form class="div-input-style" id="${id}" method="${method}" action="${action}">
<#nested/>
</form>
</#macro>

<#macro text label name id="" value="" required=false emptiable="yes">
<li class="${name?replace('.', '-')}_wrapper">
<label <#if id!="">for="${id}"</#if> <#if name!="">name="${name}"</#if>>${label}</label>
<input <#if id!="">id="${id}"</#if> <#if name!="">name="${name}"</#if> type="text" value="${value}" emptiable="${emptiable}"/><#if required==true><span class="required-indicator">*</span></#if><#nested/>
</li>
</#macro>

<#macro header label>
<li class="label ui-helper-clearfix">
<label>${label}</label>
</li>
</#macro>

<#macro textarea label name id="" required=false emptiable="yes">
<li class="${name?replace('.', '-')}_wrapper">
<label <#if id!="">for="${id}"</#if> <#if name!="">name="${name}"</#if>>${label}</label>
<textarea <#if id!="">id="${id}" </#if> <#if name!="">name="${name}"</#if>><#nested/></textarea><#if required==true><span class="required-indicator">*</span></#if>
</li>
</#macro>

<#macro select label name id="">
<li class="${name?replace('.', '-')}_wrapper">
<label for="${name}">${label}</label>
<select <#if id!="">id="${id}" </#if>name="${name}">
<#nested/>
</select>
</li>
</#macro> 

<#macro checkbox label name id="" checked=true value=true>
<li class="${name?replace('.', '-')}_wrapper">
<label for="${name}">${label}</label>
<input type="checkbox" <#if id!="">id="${id}" </#if>name="${name}" value="${value?string}" checked="${checked?string}"/><br/>
</li>
</#macro>

<#macro radio_group label id>
<li <#if id!="">class="${id}_wrapper"</#if>>
<label>${label}</label>
<div class="radio-group-horizontal ui-helper-clearfix" id="${id}">
<#nested/>
</div>
</li>
</#macro>

<#macro radio label name id value checked=false>
<input type="radio" id="${id}" value="${value}" name="${name}" <#if checked>checked="checked"</#if> /><label for="${id}">${label}</label>
</#macro>

<#macro button label id text>
<li class="ui-helper-clearfix">
<label for="${id}"><button id="${id}" type="button">${text}</button>${label}</label>
</li>
</#macro>

<#macro hidden name value="" id="id">
<li class="${name?replace('.', '-')}_wrapper" style="display:none;">
<label></label>
<input <#if id!="">id="${id}"</#if> type="hidden" name="${name}" value="${value}"></input>
</li>
</#macro>

<#macro custom label="" wrapper="">
<li class="ui-helper-clearfix<#if wrapper!=""> ${wrapper}_wrapper</#if>">
<label>${label}</label><#nested/>
</li>
</#macro>

<#macro constant label value name="" href="" default="">
<li class="ui-helper-clearfix" <#if name!="">class="${name?replace('.', '-')}_wrapper"</#if>>
<label>${label}</label>
<#if name!="">
<input type="hidden" name="${name}" value="${value}"></input>
</#if>
<#if value="" && default!="">
<#local value=default/>
</#if>
<#if href=="">
<span class="constant">${value}</span>
<#else>
<a href="${href}"><span class="constant">${value}</span></a>
</#if>
</li>
</#macro>

<#macro submit label id>
<li class="submit">
<div class="left form-message"> </div><input type="submit" class="right" id="${id}" value="${label}"/>
</li>
</#macro>

<#macro fieldset legend>
<fieldset>
<legend>${label}</legend>
<#nested/>
</fieldset>
</#macro>

<#macro field id label checked=false>
<input type="radio" id="${id}" name="${fieldsetid}" <#if checked>checked="checked" style="clear:none;"</#if>/><label for="${id}" style="clear:none;">${label}</label>
</#macro>
