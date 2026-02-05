/**
 * 
 */

/* 주문자 정보 가져오기 */
$('.check').click(function(){
	let name = $('input[name=hiddenName]').val();
	let phone = $('input[name=hiddenPhone]').val();
	let memberAddress = $('input[name=hiddenAddress]').val();
	let addressArr = memberAddress.split('$');
	let postalCode = addressArr[0];
	let address = addressArr[1];
	let detailAddress = addressArr[2];
	
	if($('.check').is(':checked')) {
		$('input[name=rcvrName]').val(name);
		$('input[name=rcvrPhone]').val(phone);
		$('input[name=postalCode]').val(postalCode);
		$('input[name=address]').val(address);
		$('input[name=detailAddress]').val(detailAddress);
    } else {
		$('input[name=rcvrName]').val('');
		$('input[name=rcvrPhone]').val('');
		$('input[name=postalCode]').val('');
		$('input[name=address]').val('');
		$('input[name=detailAddress]').val('');
    }
});

/* 주소 API */
function DaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            let detail = document.getElementById('detailAddress').value;
            document.getElementById('postalCode').value = data.zonecode;
            document.getElementById('address').value = data.roadAddress;
            if(data.buildingName != '') {
                document.getElementById('detailAddress').value = data.buildingName;
            }
            document.getElementById('detailAddress').focus();
        }
    }).open();
}

/* 드롭다운 옵션 선택 시 */
$('.dropdown .option').on('click', function() {
	let text = $(this).data('display-text') || $(this).html();
	$(this).closest('.dropdown').find('.current').html(text); //선택값 반영
	
	if(text == '직접 입력') {
		$('#typeOwnMessage').prop('hidden', false);
	} else {
		$('#typeOwnMessage').prop('hidden', true);
	}
});

let link = document.location.href;
if (link.includes('order')) {
	let totalPrice = document.querySelectorAll('.orderPrice');
	let totalOrderAmount = 0;
	
	//주문금액
	totalPrice.forEach(priceEl => {
		let price = Number(priceEl.getAttribute('value'));

		totalOrderAmount += price;
	});
	console.log("totalOrderAmount: ", totalOrderAmount);
	
	//결제금액 ( 적립금 나중에 추가 )
	//let reserveToUse 
	let deliveryFee = (totalOrderAmount >= 50000) ? 0 : 3500;
	let paymentAmount = totalOrderAmount + deliveryFee;
	console.log("paymentAmount: ", paymentAmount);
	
	document.querySelector('.order-amount').innerHTML = totalOrderAmount.toLocaleString('ko-KR') + '원';
	document.querySelector('.delivery-fee').innerHTML = deliveryFee.toLocaleString('ko-KR') + '원';
	document.querySelector('.payment-amount').innerHTML = paymentAmount.toLocaleString('ko-KR');
	document.querySelector('.payment-amount').attributes.value = paymentAmount;
	
} 

/* 주문서 약관 전체 선택 */
$(document).on('click', 'input[id=checkAll]', function(){
	if($('#checkAll').is(':checked')) {
	console.log('테스트');
        $('.term').prop('checked', true);
    } else {
		$('.term').prop('checked', false);
    }
});

$('.term').click(function(){
    if($('input[name=checkTerm]:checked').length == $('.term').length) {
        $('.checkAll').prop('checked', true);
    } else {
        $('.checkAll').prop('checked', false);
    }
});

