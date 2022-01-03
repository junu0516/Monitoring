$.support.cors = true; //크로스 브라우징 허용


let selectedCert = new Object();

$.support.cors = true;
$(window).resize(function () {
  $('#inputDiv').css('height', (650 / 875) * $(window).height() * 0.93);
  $('#inputDiv2').css('height', (650 / 875) * $(window).height() * 0.93);
  $('#outputView').css('height', (650 / 875) * $(window).height() * 0.93);
  $('#outputDiv').css('height', (650 / 875) * $(window).height() * 0.93);
});

$(function () {
  $('#tx_input').val('{"appCd": "InfotechApiDemo", "orgCd": "hometax", "svcCd": "Z0001", "bizNo": "1388148652"}');

  $('#signPw').keydown(function (key) {
    if (key.keyCode == 13) {
      selectedCertExecution();
    }
  });
});

function selectedCertExecution() {
  selectedCert.signPw = $('#signPw').val();
  if (selectedCert.signCert === undefined || selectedCert.signCert === null) {
    alert('인증서를 선택해주세요');
  } else if (selectedCert.signPw === undefined || selectedCert.signPw === null || selectedCert.signPw === '') {
    alert('인증서 비밀번호를 입력해주세요');
  } else {
    loadDoc('execute', JSON.stringify(selectedCert), 16566);
    selectedCertReset();
  }
}

function selectedCertReset() {
  $('#tbody').empty();
  $('#signPw').val('');
  $('.dim-layer').fadeOut('fast');
  selectedCert = new Object();
}

function loadDoc(s_op, s_inJson, port, success) {
  const url = 'https://127.0.0.1:' + port + '/?op=' + s_op;

  $('#tx_output').val('---------- send ----------');
  $.ajax({
    type: 'POST',
    url: url,
    data: s_inJson || '{}',
    crossDomain: true,
    crossOrigin: true,
    dataType: 'json',
    contentType: 'application/json; charset=UTF-8',
    success: function (data) {
      printReceive(data);
      if (null != success) success(data);
    },
    error: function (xhr, status, error) {
      $('#tx_output').val('status : ' + status + '\nerror : ' + error);

      if ('setup' == s_op) {
        alert('프로그램 설치가 필요합니다.');
        fnNxDownload();
      } else {
        // 에러 처리 로직
      }
    },
  });
}

function popup() {
  layerPopup('#layer');

  let success = function (data) {
    data.list.forEach(function (item) {
      let tr = document.createElement('tr');
      tr.setAttribute('onclick', 'selectNode(this)');
      //구분
      let td = document.createElement('td');
      td.setAttribute('style', 'width:auto');
      td.innerText = distingCert(item.oid);
      tr.appendChild(td);

      //인증서명
      td = document.createElement('td');
      td.setAttribute('style', 'width:230px;text-overflow:ellipsis;');
      td.innerText = item.certName;
      tr.appendChild(td);

      // 만료일
      td = document.createElement('td');
      td.setAttribute('style', 'width:100px;');
      td.innerText = item.toDt;
      tr.appendChild(td);

      //발급자
      td = document.createElement('td');
      td.setAttribute('style', 'width:100px;');
      td.innerText = item.pub;
      tr.appendChild(td);

      //위치
      td = document.createElement('td');
      td.setAttribute('style', 'width:40px;');
      td.innerText = item.drive;
      tr.appendChild(td);

      //Path (숨김처리)
      td = document.createElement('td');
      td.setAttribute('style', 'display:none');
      td.innerText = item.path;
      tr.appendChild(td);

      document.getElementById('tbody').appendChild(tr);
    });

    $('#tbody td').css('border', '1px solid rgba(128, 128, 128, 0.2)');
  };

  loadDoc('certList', setCertSelect(), 16566, success);
}

function layerPopup(el) {
  var $el = $(el);
  $('.dim-layer').fadeIn('fast');
  
  let $elWidth = ~~$el.outerWidth();
  let $elHeight = ~~$el.outerHeight();
  let docWidth = $(document).width();
  let docHeight = $(document).height();

  if ($elHeight < docHeight || $elWidth < docWidth) {
    $el.css({
      marginTop: -$elHeight / 2,
      marginLeft: -$elWidth / 2,
    });
  } else {
    $el.css({ top: '50%', left: '50%'});
  }
}

function printReceive(data) {
  try {
    const jsonPretty = JSON.stringify(JSON.parse(JSON.stringify(data)), null, 2);
    $('#tx_output').val(jsonPretty);
  } catch (e) {
    $('#tx_output').val(data);
  }
}

function selectNode(el) {
  $('#tbody tr').css('background-color', 'white');
  $(el).css('background-color', '#ccc');

  const path = el.children[5].innerText;
  selectedCert.orgCd = 'common';
  selectedCert.svcCd = 'getCertInfo';
  selectedCert.appCd = 'InfotechApiDemo';
  selectedCert.signCert = path + '\\signCert.der';
  selectedCert.signPri = path + '\\signPri.key';
}

function setCertSelect() {
  // sample
  // $('#tx_input').val('{"certImageUrl": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_92x30dp.png", "nxKeypad": ""}');
  $('#tx_input').val('{"certImageUrl": "", "nxKeypad": ""}');
  return $('#tx_input').val();
}

function fnNxDownload() {
  if (!$('#ifrFile').length) {
    $('body').append($('<iframe />', { id: 'ifrFile', style: 'display:none;' }));
  }
  $('#ifrFile').attr('src', './ExAdapter_Web_Setup_20210225.exe');
}