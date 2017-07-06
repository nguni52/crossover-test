<html>
<head>
    <title>Notify of Journals</title>
</head>
<body>
Hello ${loginName}<br>

We have new journals that were published today. <br/>

The names of the journals are:

<ul>
    <#list journals as journal>
        <li>${journal.name} - ${journal.description}</li>
    </#list>
</ul>

<br/>
Kind Regards,<br/>
The Team
</body>
</html>