function update7dayBefore(){

  var strStartDate = document.getElementById('startDate').value;
  var dtStartDate = new Date(strStartDate)
  // 7日前の日付をセット
  var hoge = dtStartDate.setDate(dtStartDate.getDate() - 7)

  var y = dtStartDate.getFullYear();
  var m = ('00' + (dtStartDate.getMonth()+1)).slice(-2);
  var d = ('00' + dtStartDate.getDate()).slice(-2);

  startDate.value = y + '-' + m + '-' + d;
  document.scheudle_param_form.submit();
}

function update7dayAfter(){

  var strStartDate = document.getElementById('startDate').value;
  var dtStartDate = new Date(strStartDate)
  // 7日後の日付をセット
  var hoge = dtStartDate.setDate(dtStartDate.getDate() + 7)

  var y = dtStartDate.getFullYear();
  var m = ('00' + (dtStartDate.getMonth()+1)).slice(-2);
  var d = ('00' + dtStartDate.getDate()).slice(-2);

  startDate.value = y + '-' + m + '-' + d;
  document.scheudle_param_form.submit();
}