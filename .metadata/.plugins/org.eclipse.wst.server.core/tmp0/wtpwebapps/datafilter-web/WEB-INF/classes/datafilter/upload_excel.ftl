<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>数据导入</title>

    <!-- Bootstrap core CSS -->
    <link href="${request.contextPath}/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="${request.contextPath}/bootstrap/fileinput/css/fileinput.min.css" rel="stylesheet">

    <script src="${request.contextPath}/js/jquery/jquery-1.11.1.min.js"></script>

    <script src="${request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
    <script src="${request.contextPath}/bootstrap/fileinput/js/fileinput.min.js"></script>
    <script src="${request.contextPath}/bootstrap/js/locales/zh.js"></script>


  </head>

  <body>
      <!-- nav -->
      <ul class="nav nav-tabs">
          <li role="presentation"><a href="${request.contextPath}/index">首页</a></li>
          <li role="presentation" class="active"><a href="${request.contextPath}/to_upload">数据导入</a></li>
          <li role="presentation"><a href="${request.contextPath}/to_upload_chaopi">朝批效期数据导入</a></li>
          <li role="presentation"><a href="${request.contextPath}/to_upload_query">朝批效期数据</a></li>
          <li role="presentation"><a href="${request.contextPath}/bi">BI</a></li>
      </ul>

      <div style="margin: 100px 100px;">
          <span style="color: #cccccc; ;">
              1、仅支持Excel;<br>
              2、请上传数据量在1万以内的Excel;<br>
              3、Excel大小请小于5M;<br>
              4、请确认Excel编码格式为UTF8;
          </span><br><br>
          <input id="localFile" name="localFile" type="file" multiple>
      </div>

  </body>


  <script>
      $(function () {
          initFileInput();
      });

      function initFileInput() {

          $('#localFile').fileinput({
              language: 'zh',
              uploadUrl: '${request.contextPath}/upload_file',
              maxFileCount: 1, //表示允许同时上传的最大文件个数
              maxFileSize: 1024*1024,//单位为kb
              validateInitialCount:true,
              resizeImage: false,
              showUpload: false,
              showCaption: false,
              showPreview: false,
              showRemove: false,//是否显示移除总按钮
              browseClass: "btn btn-primary btn-lg",
              previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
              msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
              allowedFileExtensions: ['xlsx','csv']
          }).on("filebatchselected", function(event, files) {
              $(this).fileinput("upload");
          }).on("fileuploaded", function (event, data) {    //一个文件上传成功
              console.log('文件上传成功！');
              console.log('文件上传成功！'+data.id);
              alert(1);
              alert(data.req_code);
              console.log('文件上传成功！'+data.id);
              if(data.req_code == "T"){
                  alert('处理成功');
              }
          }).on('filepreupload', function(event, data, previewId, index) {     //上传中
              var form = data.form, files = data.files, extra = data.extra,
                      response = data.response, reader = data.reader;
              console.log('文件正在上传');
          }).on('fileerror', function(event, data, msg) {  //一个文件上传失败
              console.log('文件上传失败！'+data.id);


          })
      }

  </script>
</html>
