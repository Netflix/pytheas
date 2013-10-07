<#macro body>

<script type="text/javascript">
<#include "home.js"/>
</script>

<style>
    .explorer-container {
        margin: 10px;
        border: 1px solid #AAA;
        width: 8em;
        height: 6em;
        float: left;
        background: #ddd;
        cursor: pointer;
        display: table
    }

    .explorer-container p {
        display : table-cell;
        font-size: 1.2em;
        text-align: center;
        vertical-align: middle;
    }

    .explorer-container:hover {
        background: #CCC;
    }

    h1 {
        font-size: 1.2em;
        margin: 10px 10px 0px 10px;
    }

    .tipsy {
        font-size: 1em;
    }

</style>

<div id="internal-explorers">
    <h1>Explorers</h1>
    <#list Explorers.explorers as explorer>
        <a class="explorer-container ui-corner-all"
           href="${RequestContext.pathToRoot}${explorer.name}/"
           rel="popover"
           data-content="<#if explorer.description?exists>${explorer.description}<#else><em>ERROR</em></#if>">
            <p><#if explorer.title?exists>${explorer.title}<#else><em>ERROR : ${explorer.name}</em></#if><#if explorer.isSecure?exists && explorer.isSecure> <i class="icon-lock"> </i> </#if></p>
        </a>
    </#list>
</div>

</#macro>