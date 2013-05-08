<#include "js_css_include.ftl"/>
<script type="text/javascript">
<#include "main.js"/>
<#include "header.js"/>
</script>

<#macro toolbar>
</#macro>
<#macro head>
</#macro>

<#macro breadcrumbs>
<div><span id="ca-breadcrumbs"></span></div>
</#macro>

<#macro body>
Missing body macro for ${nestedpage}
</#macro>

<#macro render_menu m prefix="">
    <#list m.children?keys as name>
        <#assign menu = m.children[name]>
        <#if menu.hasChildren()>
            <li class="menuButton dropdown"><a class="menu-${prefix}${menu.name}"><span>${menu.title}</span></a>
            <ul><@render_menu menu prefix+menu.name+"-"/></ul>
            </li>
        <#else>
            <li class="menuButton"><a href="${RequestContext.pathToRoot}${Explorer.name}/${menu.href}" class="menu-${prefix}${menu.name}">${menu.title}</a></li>
        </#if>
    </#list>
</#macro>

<#include "${nestedpage}">

<html>
  <head>
    <@head/>
  </head>
  <body>
    <div class="outer-north">
        <#include "header.ftl"/>
        <#if Explorer.menu?exists>
            <ul class="nav navcontainer">
                <@render_menu Explorer.menu/>
            </ul>
            <div class="toolbarcontainer">
                <div class="toolbarcustom">
                    <@toolbar/>
                </div>
            </div>
        </#if>
    </div> 
   
    <div class="outer-center">
<@body/>
    </div>
    
    <div class="outer-south env-${Global.environmentName}">
        <#include "footer.ftl"/>
    </div>
    
  </body>
</html>

