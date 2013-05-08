<#macro body>
<div style="margin: 10px">

<h1>Internal Error : <#if exception.response?exists>${exception.response.getStatus()}</#if></h1>

<p/><h1>Exception</h1>
<#if exception.message?exists>
${exception.message}
<#else>
null
</#if>
<p/><h1>Stack Trace</h1>
<#list exception.getStackTrace() as item>
<br/>${item}
</#list>
<#if exception.cause?exists>
    ${exception.cause}
    <p/><h1>Cause By</h1>
    <#list exception.cause.getStackTrace() as item>
    <br/>${item}
    </#list>
</#if>
</div>
</#macro>