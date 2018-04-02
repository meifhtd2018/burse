<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>测试页面</title>
</head>
<body>
body内容
<hr>
哇哇哇~:<#if name??>${name}</#if>

<#if customer??>
    <#if customer.address??>
        ${customer.address}<br>
    </#if>

    <#if customer.cid??>
        ${customer.cid}<br>
    </#if>

    <#if customer.idNumber??>
        ${customer.idNumber}<br>
    </#if>

    <#if customer.mobile??>
        ${customer.mobile}<br>
    </#if>

    <#if customer.name??>
        ${customer.name}<br>
    </#if>
</#if>
<hr>
我是不讲理的匿名客户<br>
我是客服<br>
用户注册<br>
客服登录<br>
退出登录<br>
<hr>
<a href="/testPage.go" target="_blank">to测试页面</a>
</body>
</html>