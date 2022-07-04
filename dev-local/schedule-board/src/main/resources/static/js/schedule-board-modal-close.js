// modalが指定時間以上、操作されなかったら閉じる

const MODAL_TIMEOUT_MILLISECONDS = 10*60*1000; // 10分
const TIMER_INTERVAL_MILLISECONDS = 0.5*60*1000;
let lastChangedTime = new Date();

// 操作した時間を更新する
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

// もし10分分以上経っていたらモーダルを閉じる
function tick() {
  const currentTime = new Date();
  if(diff_time(currentTime, lastChangedTime) >= MODAL_TIMEOUT_MILLISECONDS){
      close_modal();
  }
}

// document.onloadとかで呼び出す
// 
function init(){
  // 前回操作した時間の初期化 new Date()で現在時刻
  lastChangedTime = new Date();
  // $('.changeform').on('change', onChangedFormContent);
  // 指定した時間ごとにtickを呼び出す
  // (setInterval 一定時間ごとに特定の処理を繰り返す)
  let timer = setInterval(tick, TIMER_INTERVAL_MILLISECONDS);
}

// 画面が読み込まれた、init()を実行する
window.addEventListener('load',function(){
  init();
});