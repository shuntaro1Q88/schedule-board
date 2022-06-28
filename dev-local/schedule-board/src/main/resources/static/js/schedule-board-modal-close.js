// modalが指定時間以上、操作されなかったら閉じる

const MODAL_TIMEOUT_MILLISECONDS = 10*60*1000; // 5分
const TIMER_INTERVAL_MILLISECONDS = 0.5*60*1000;
let lastChangedTime = new Date();

function onChangedFormContent() {
    lastChangedTime = new Date();
}

function close_modal(){
  /// もしもモーダルが開いていたら、閉じる
  if($('#myModal').hasClass('show')){
    $('#myModal').modal('hide');
  } 
}

function diff_time(timeA, timeB){
    if(timeA > timeB){
        return timeA - timeB;
    }else{
        return timeB - timeA;
    }
}

function tick() {
    // もし5分以上経っていたらモーダルを閉じる
    const currentTime = new Date();
    if(diff_time(currentTime, lastChangedTime) >= MODAL_TIMEOUT_MILLISECONDS){
        close_modal();
    }
}

// document.onloadとかで呼び出す
function init(){
  lastChangedTime = new Date();
  // $('.changeform').on('change', onChangedFormContent);
  let timer = setInterval(tick, TIMER_INTERVAL_MILLISECONDS);    
}

window.addEventListener('load',function(){
  init();
});