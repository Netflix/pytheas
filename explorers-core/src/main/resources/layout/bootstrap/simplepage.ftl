<#macro pagelayout title section_header="" icon="">

<script type="text/javascript">
<#include "simpelpage.js"/>
</script>

<style>
.dataTables_wrapper {
    margin: 6px 6px 20px 6px; 
    position: relative;
    min-height: 100px;
    _height: 152px;
    clear: both;
}

.dataTables_wrapper td {
    word-wrap: break-word;
}

.dataTables_wrapper table {
    table-layout:fixed
}

</style>

<div class="mini-layout">
    <div class="section-breadcrumbs">
        <ul class="breadcrumb">
            <#if RequestContext.contextPath=="">
                <li><a href="/"><i class="icon-home"></i></a><span class="divider">/</span></li>
            <#else>
                <li><a href="${RequestContext.contextPath}">Home</a><span class="divider">/</span></li>
            </#if>
            <li><a href="${RequestContext.pathToRoot}${Explorer.name}">${Explorer.title}</a><span class="divider">/</span></li>
        </ul>
    </div>
    <div class="section-header ui-helper-clearfix">
        <div class="left">
            <h1>${title}</h1>
        </div>
        <div class="right">
            ${section_header}
        </div>
    </div>
    <div class="section-contents clear">
    <#nested/>
    </div>
    <div class="section-footer clear">
    </div>
</div>

</#macro>