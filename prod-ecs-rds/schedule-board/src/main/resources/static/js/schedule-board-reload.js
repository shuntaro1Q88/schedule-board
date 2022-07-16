/* ページリロード */

const timer = 15 * 60 * 1000;    // ミリ秒で間隔の時間を指定
function checkAndReload() {
  if($('#myModal').hasClass('show')){
    // モーダル開いていたら
    console.log("モーダルが開いている為 reload を抑止しました。")
  } else {
    // モーダル開いていなければreloadする
    location.reload();
  }
}
window.addEventListener('load',function(){
  setInterval('checkAndReload()',timer);
});