/**
 * 
 */

/* 장바구니 항목 전체 선택 */
$(document).on('click', 'input[name=checkAll]', function() {
	let isChecked = $(this).prop('checked'); // 체크 상태 확인
	$('.item').prop('checked', isChecked);   // tbody 안 모든 체크박스 동기화
});

/* 수량 증가 */
$(document).on('click', '.upBtn', function() {
	let selectedAmount = $(this).closest('.countBox').find('input[name=selectedAmount]').val(); //input
	console.log("selectedAmount : " + selectedAmount);
	let count = parseInt(selectedAmount);
	count++;

	//클릭 시 색상 변경으로 활성화 표시
	if (count > 1) {
		$(this).closest('td').find('.downBtn').prop('disabled', false);
	}

	$(this).attr('style', 'color: #000;');
	$(this).closest('td').find('.downBtn').attr('style', 'color: #000;');
	$(this).closest('td').find('.modifyBtn').attr('style', 'color: #000; border: 1px solid #000;');

	$(this).closest('.countBox').find('input[name=selectedAmount]').val(count); //증가한 수량 대입

	//수량 증가에 따른 주문금액 반영
	let originalPrice = $(this).closest('tr').find('.prodPrice').attr('value');
	console.log(originalPrice);
	let price = parseInt(originalPrice);
	let result = count * price;
	$(this).closest('tr').find('.orderPrice').attr('value', result);
	$(this).closest('tr').find('.orderPrice').text(result.toLocaleString('ko-KR') + "원"); //원화 단위로 출력
});



/* 수량 감소 */
$(document).on('click', '.downBtn', function() { //down 버튼
	let selectedAmount = $(this).closest('.countBox').find('input[name=selectedAmount]').val(); //input
	//console.log("selectedAmount : " + selectedAmount);
	let count = parseInt(selectedAmount);
	count--;
	if (count == 1) {
		$(this).closest('td').find('.downBtn').prop('disabled', true);
	} else {
		$(this).attr('style', 'color: #000;');
		$(this).closest('td').find('.upBtn').attr('style', 'color: #000;');
		$(this).closest('td').find('.modifyBtn').attr('style', 'color: #00008b; border: 1px solid #000;');
	}

	$(this).closest('.countBox').find('input[name=selectedAmount]').val(count); //감소한 수량 대입

	//수량 감소에 따른 주문금액 반영
	let originalPrice = $(this).closest('tr').find('.prodPrice').attr('value');
	console.log(originalPrice);
	let price = parseInt(originalPrice);
	let result = count * price;
	$(this).closest('tr').find('.orderPrice').attr('value', result);
	$(this).closest('tr').find('.orderPrice').text(result.toLocaleString('ko-KR') + "원"); //원화 단위로 출력
});

/* 수량 입력 */
$(document).on('change', 'input[name=selectedAmount]', function() { //input 값 변경
	let selectedAmount = $(this).closest('.countBox').find('input[name=selectedAmount]').val(); //input
	let count = parseInt(selectedAmount);
	if (count > 1) {
		$(this).closest('td').find('.downBtn').prop('disabled', false);
	} else if (count == 1) {
		$(this).closest('td').find('.downBtn').prop('disabled', true);
	}

	$(this).closest('td').find('.upBtn').attr('style', 'color: #000;');
	$(this).closest('td').find('.downBtn').attr('style', 'color: #000;');
	$(this).closest('td').find('.modifyBtn').attr('style', 'color: #000; border: 1px solid #000;');

	//수량 입력에 따른 주문금액 반영
	let originalPrice = $(this).closest('tr').find('.prodPrice').attr('value');
	console.log(originalPrice);
	let price = parseInt(originalPrice);
	let result = count * price;
	$(this).closest('tr').find('.orderPrice').attr('value', result);
	$(this).closest('tr').find('.orderPrice').text(result.toLocaleString('ko-KR') + "원"); //원화 단위로 출력
});

/* 수량 변경사항 저장 */
$(document).on('click', '.modifyBtn', function() { //modify 버튼
	let selectedAmount = $(this).closest('tr').find('input[name=selectedAmount]').val(); //input
	let optCombNo = $(this).closest('tr').find('.option-area').attr('value');
	console.log(optCombNo);

	let param = { quantity: selectedAmount, optCombNo: optCombNo };

	$.ajax({
		url: '/cart/mycart/modify',
		type: 'post',
		data: JSON.stringify(param),
		beforeSend: function(xhr) {
			xhr.setRequestHeader("Accept", "application/json");
			xhr.setRequestHeader("Content-Type", "application/json");
		},
		success: function(result) {
			Swal.fire({
				icon: 'success',
				title: '수량 변경이 완료되었습니다',
				confirmButtonColor: '#00008b',
				confirmButtonText: '확인'
			}).then((result) => {
				if (result.isConfirmed) {
					window.location.reload(); //페이지 새로고침
					window.history.scrollRestoration = 'manual'; //스크롤 최상단 고정
				}
			})
		},
		error: function(status, error) { console.log(status, error); }
	});
});

let link = document.location.href;
if (!link.includes('order')) {

	/* 합계-상품금액 */
	let prodPrice = document.querySelectorAll('.origPrice');
	let orderPrice = 0;

	prodPrice.forEach(priceEl => {
		let row = priceEl.closest('tr');
		let quantity = Number(row.querySelector('.selectedAmount').value);
		let price = Number(priceEl.getAttribute('value'));

		orderPrice += price * quantity;
	});
	console.log("orderPrice:", orderPrice);

	/* 합계-상품할인금액 */
	let discounted = document.querySelectorAll('.discounted');
	let discounts = document.querySelectorAll('del');
	let discountAmount = 0;

	discounted.forEach((priceEl, i) => {
		let row = priceEl.closest('tr'); // tr 행 가져오기
		let quantity = Number(row.querySelector('.selectedAmount').value);
		let originalPrice = Number(discounts[i].getAttribute('value'));
		let salePrice = Number(priceEl.getAttribute('value'));

		discountAmount += (originalPrice * quantity) - (salePrice * quantity);
	});

	console.log("discountAmount:", discountAmount); // 총 할인금액

	/* 배송비 */
	let deliveryFee = 3500;

	if (orderPrice - discountAmount >= 50000) {
		deliveryFee = 0;
	} else {
		deliveryFee = 3500;
	}

	/* 합계 반영 */
	let totalPrice = orderPrice - discountAmount + deliveryFee;
	console.log("deliveryFee: ", deliveryFee);
	console.log("totalPrice: ", totalPrice);
	document.querySelector('.order-price').innerHTML = orderPrice.toLocaleString('ko-KR');
	document.querySelector('.discount-amount').innerHTML = discountAmount.toLocaleString('ko-KR');
	document.querySelector('.delivery-fee').innerHTML = deliveryFee.toLocaleString('ko-KR');
	document.querySelector('.total-price').innerHTML = totalPrice.toLocaleString('ko-KR');
}

/* 개별 상품 삭제 */
$(document).on('click', '.deleteBtn', function() {
	let optCombNo = $(this).closest('tr').find('.option-area').attr('value');
	let param = { optCombNo, optCombNo };

	$.ajax({
		url: '/cart/mycart/delete',
		type: 'post',
		data: JSON.stringify(param),
		beforeSend: function(xhr) {
			xhr.setRequestHeader("Accept", "application/json");
			xhr.setRequestHeader("Content-Type", "application/json");
		},
		success: function(result) {
			Swal.fire({
				icon: 'success',
				title: '선택 상품이 삭제되었습니다',
				confirmButtonColor: '#00008b',
				confirmButtonText: '확인'
			}).then((result) => {
				if (result.isConfirmed) {
					window.location.reload(); //페이지 새로고침
					window.history.scrollRestoration = 'manual'; //스크롤 최상단 고정
				}
			})
		},
		error: function(status, error) { console.log(status, error); }
	});
});

/* 선택 상품 삭제 */
$(document).on('click', '.button-delete', function() {
	let checkbox = $('input[name=checkItem]:checked');
	let arr = new Array();

	checkbox.each(function() {
		let row = $(this).closest('tr');
		let optCombNo = Number(row.find('.option-area').attr('value'));
		console.log("optCombNo:", optCombNo);
		arr.push(optCombNo);
	});

	if (arr.length == 0) {
		Swal.fire({
			icon: 'warning',
			title: '1개 이상의 상품을 체크하세요',
			confirmButtonColor: '#00008b',
			confirmButtonText: '확인'
		}).then((result) => {
			if (result.isConfirmed) {
				history.go(0); //현재 페이지 새로고침
			}
		})
	}

	$.ajax({
		url: '/cart/mycart/deleteCheck',
		type: 'post',
		contentType: 'application/json',
		data: JSON.stringify(arr),
		success: function(result) {
			Swal.fire({
				icon: 'success',
				title: '선택 상품이 삭제되었습니다',
				confirmButtonColor: '#00008b',
				confirmButtonText: '확인'
			}).then((result) => {
				if (result.isConfirmed) {
					window.location.reload(); //페이지 새로고침
					window.history.scrollRestoration = 'manual'; //스크롤 최상단 고정
				}
			})
		},
		error: function(status, error) { console.log(status, error); }
	});

});

/* 장바구니 상품 주문 */
function orderAll() {
	let checkbox = $('input[name=checkItem]');
	let arr = new Array();

	checkbox.each(function() {
		let row = $(this).closest('tr');
		let optCombNo = Number(row.find('.option-area').attr('value'));
		console.log("optCombNo:", optCombNo);
		arr.push(optCombNo);

	});
	$.ajax({
			url : '/cart/order',
			type : 'get',
			traditional : true, //배열 넘기기 위한 세팅
			dataType : 'text',
			data : { arr : arr },
			success : function(result){
				console.log('주문페이지 이동');
				window.location.replace('/cart/order'); // 히스토리 추가 안됨, 한 페이지만 이동
				//location.href='/cart/order';
			},
			error : function(status, error){ console.log(status, error); }
		});
}



