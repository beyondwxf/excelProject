<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>简单操作</title>

    <!-- Bootstrap -->
    <link href="${request.contextPath}/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="${request.contextPath}/bootstrap/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="${request.contextPath}/bootstrap/timer/bootstrap-datetimepicker.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="${request.contextPath}/static/bi/css/font-awesome.min.css" rel="stylesheet">
    <!-- NProgress -->
    <link href="${request.contextPath}/static/bi/css/nprogress.css" rel="stylesheet">
    <!-- iCheck -->
    <link href="${request.contextPath}/static/bi/css/green.css" rel="stylesheet">

    <!-- Custom Theme Style -->
    <link href="${request.contextPath}/static/bi/css/custom.min.css" rel="stylesheet">
  </head>

  <body>

  <!-- nav -->


    <!-- page content -->
 <form class="" role="form">
        <div class="">
            <div class="row text-center" style="padding: 20px 0;">
                <h3>测试环境常用币种操作</h3>
            </div>
            <div class="row" style="margin-left: 40px;">
            	  <div class="form-group col-lg-2">
                    <div class="input-group">
                        <span class="input-group-addon">操作币种</span>
                        <select name="input_province" id="input_province" class="form-control">
                            <option>请选择</option>
                        </select>
                    </div>
                </div>
                <div class="form-group col-lg-2">
                    <div class="input-group">
                        <span class="input-group-addon">操作类型</span>
                        <select name="input_city" id="input_city"  class="form-control">
                            <option>请选择</option>
                        </select>
                    </div>
                </div>
                <!--前缀-->
               <div class="form-group col-lg-2">
                    <div class="input-group">
                        <span class="input-group-addon">大网ID</span>
                        <input class="form-control" type="text" id="hexunid">
                    </div>
                </div>
                 <div class="form-group col-lg-4">
                    <div class="input-group">
                        <span class="input-group-addon">操作金额(分)</span>
                        <input class="form-control" type="text" id="amount">
                    </div>
                </div>
               
              <button type="button" class="btn btn-success" onclick="submitPoint();">提交</button>
        </div>
        <div class="form-group row" style="padding: 20px 0;margin-left: 40px;margin-right: 40px;" >
		    <label for="name">结果</label>
		    <textarea class="form-control" rows="6" id="resulttextarea"></textarea>
  		</div>
        	
    </form>
    <!-- /page content -->

    <!-- jQuery -->
    <script src="${request.contextPath}/js/jquery/jquery-1.11.1.min.js"></script>
    <!-- Bootstrap -->
    <script src="${request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
    <script src="${request.contextPath}/bootstrap/js/bootstrap-select.min.js"></script>
    <script src="${request.contextPath}/bootstrap/timer/bootstrap-datetimepicker.min.js"></script>
    <script src="${request.contextPath}/bootstrap/timer/bootstrap-datetimepicker.zh-CN.js"></script>
    <!-- FastClick -->
    <script src="${request.contextPath}/static/bi/js/fastclick.js"></script>
    <!-- NProgress -->
    <script src="${request.contextPath}/static/bi/js/nprogress.js"></script>
    <!-- morris.js -->
    <script src="${request.contextPath}/static/bi/js/raphael.min.js"></script>
    <script src="${request.contextPath}/static/bi/js/morris.min.js"></script>


  <script>

      $(function () {
        		var pdata = [{"code":"110000","level":"0","names":"牛币","value":"BULL"},{"code":"120000","level":"0","names":"神币","value":"SUPGOD"},{"code":"130000","level":"0","names":"金币","value":"GOLD"},{"code":"110100","level":"1","names":"查询","value":"QUERY"},{"code":"110200","level":"1","names":"增加","value":"ADD"},{"code":"110300","level":"1","names":"减少","value":"MINUS"},{"code":"110400","level":"1","names":"清零","value":"RESET"},{"code":"120100","level":"1","names":"查询","value":"QUERY"},{"code":"120200","level":"1","names":"增加","value":"ADD"},{"code":"120300","level":"1","names":"减少","value":"MINUS"},{"code":"120400","level":"1","names":"清零","value":"RESET"},{"code":"130100","level":"1","names":"查询","value":"QUERY"},{"code":"130200","level":"1","names":"增加","value":"ADD"},{"code":"130300","level":"1","names":"减少","value":"MINUS"}];
        
        		var html = "";
                $("#input_city").append(html); 
                $.each(pdata,function(idx,item){
                    if (parseInt(item.level) == 0) {
                        html += "<option value='" + item.value + "' exid='" + item.code + "'>" + item.names + "</option>";
                    }
                });
                $("#input_province").append(html);
                $("#input_province").change(function(){
                    if ($(this).val() == "") return;
                    $("#input_city option").remove();
                    var code = $(this).find("option:selected").attr("exid"); 
                    var code = code.substring(0,2);
                    var html = "<option value=''>请选择</option>"; 
                    $.each(pdata,function(idx,item){
                        if (parseInt(item.level) == 1 && code == item.code.substring(0,2)) {
                            html += "<option value='" + item.value + "' exid='" + item.code + "'>" + item.names + "</option>";
                        }
                    });
                    $("#input_city").append(html);
                });
      })

      // 提交表单
      function submitPoint(){
           var input_province = $("#input_province").val();
          var input_city = $("#input_city").val();
          var hexunid = $("#hexunid").val();
          var amount = $("#amount").val();
          if(input_province.length<1 || input_city.length<1 || hexunid.length<1){
             alert("请正确选择相应操作!");
             return;
          }else if(("ADD"==input_city||"MINUS"==input_city)&(amount.length<1)){
             alert("请输入正确金额!");
             return;
          }
           $.get("${request.contextPath}/pointOperation",{
               "pointType": input_province,
               "pointOperation": input_city,
               "userid": hexunid,
               "amout": amount
           },function (data) {
               if(data.respCode == 'T'){
 //                alert("zhengque");
//                  alert(data.respData);
                  $("#resulttextarea").text(data.respData);
               }else{
               
                  alert("返回错误");
                  // alert(data.respData);
               }
           })
          
      
      }


  </script>

  </body>
</html>