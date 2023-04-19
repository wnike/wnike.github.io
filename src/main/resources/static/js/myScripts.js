// function skipPage(obj) {
//     var href = $(obj).attr("href"); //获取请求的url
//     var pageName = $(obj).attr("data-skip-view");//即将跳转的界面名称
//     var data = {"viewName": pageName};
//     //ajax 请求
//     $.ajax({
//         url: href,
//         data: data,
//         type: "post",
//         success: function (data) {
//             $("#right").html(data);  //请求的界面数据在右侧显示
//         }
//     });
// }
