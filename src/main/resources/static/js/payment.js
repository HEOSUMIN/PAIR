/**
 * 
 */
function requestPay() {
	let memberId = $('input[name=memberId]').val();
	let memberPhone = $('input[name=phone]').val();
	let memberEmail = $('input[name=email]').val();

	let postalCode = $('input[name=postalCode]').val();
	let address = $('input[name=address]').val() + ' ' + $('input[name=detailAddress]').val();
	let message = $('#optionSelector .current').text();
	if (message == '직접 입력') {
		message = $('input[name=typeOwnMessage]').val();
	}

	let method = $('input[name=methods]:checked').val();
	/*let amount = parseInt(document.querySelector('.payment-amount').innerHTML.replace(',', ''));*/
	let amount = 1;
	let orderName = document.querySelector('.option-area a:first-child').textContent;

	let productCount = document.querySelectorAll('.product-table tbody tr').length; //주문상품 개수
	if (productCount > 1) { //주문상품이 2개 이상인 경우
		orderName = orderName + ' 외 ' + (productCount - 1) + '건';
	}
	let paymentNo = 'P' + new Date().getTime() + memberId.toUpperCase();
	
	let rcvrName = $('input[name=rcvrName]').val();
	let rcvrPhone = $('input[name=rcvrPhone]').val();

	let pointAmount = $('input[name=reserve]').val().replace(',', '');
	let deliveryFee = parseInt(document.querySelector('.delivery-fee').innerHTML.replace(',', '').slice(0, -1));

	let items = [];
	let options = document.querySelectorAll('.option-area');
	let quantities = document.querySelectorAll('.quantity-area');
	let prices = document.querySelectorAll('.orderPrice');

	//장바구니 삭제용 옵션번호 배열
	let arr = [];

	for (let i = 0; i < options.length; i++) {
		items.push({
			optCombNo: parseInt(options[i].dataset.optionNo),
			quantity: parseInt(quantities[i].dataset.quantity),
			price: parseInt(prices[i].dataset.price)
		});
		arr.push(parseInt(options[i].dataset.optionNo));
	}

	var IMP = window.IMP;
	IMP.init('imp42653157');
	IMP.request_pay({
		pg: 'html5_inicis',
		pay_method: method,
		merchant_uid: paymentNo,
		name: orderName,
		amount: amount,
		buyer_email: memberEmail,
		buyer_name: memberId,
		buyer_tel: memberPhone,
		buyer_addr: address,
		buyer_postcode: postalCode
	}, function(rsp) {
		console.log(rsp);

		//결제 검증
	/*	$.ajax({
			url: '/verifyiamport/' + rsp.imp_uid,
			type: 'post'
		}).done(function(data) {
			console.log(data);

			if (rsp.paid_amount == data.response.amount) { //클라이언트-서버간 결제금액 비교
	*/			console.log('결제 및 검증 완료');

				let params = {
					items: items,
					memberId: memberId,
					rcvrName: rcvrName,
					rcvrPhone: rcvrPhone,
					rcvrPostalCode: postalCode,
					rcvrAddress: address,
					dlvrReqMessage: message,
					deliveryFee: deliveryFee,
					pointAmount: pointAmount,
					paymentMethod: method,
					paymentAmount: amount
				};
				console.log("params: ", params);

				$.ajax({
					url: "/cart/order",
					type: "POST",
					contentType: 'application/json',
					data: JSON.stringify(params),
					success: function(res) {
						console.log("res.result: ", res.result);
						if (result == 'succeed') {
							Swal.fire({
								icon: 'success',
								title: '주문 및 결제가 완료되었습니다',
								confirmButtonColor: '#00008b',
								confirmButtonText: '확인'
							}).then((result) => {
								if (result.isConfirmed) {
									//장바구니 목록에서 삭제
									$.ajax({
										url: '/cart/mycart/deleteCheck',
											type: 'post',
											contentType: 'application/json',
											data: JSON.stringify(arr),
											success: function(result) {
											console.log('장바구니에서 주문/결제 완료 상품 삭제 완료');
										},
										error : function(status, error){ console.log(status, error); }
									}).done(function() { location.href='/'; });
								}
							})
						}
					},
					error: function(status, error) { console.log(status, error); }
				});
	/*		} else {
				var msg = '결제에 실패하였습니다.';
				msg += '에러내용 : ' + rsp.error_msg;
				Swal.fire({
					icon: 'error',
					title: '잠시 후 다시 시도해 주세요',
					text: msg,
					confirmButtonColor: '#00008b',
					confirmButtonText: '확인'
				}).then((result) => {
					if (result.isConfirmed) {
						window.location.reload(); //페이지 새로고침
						window.history.scrollRestoration = 'manual'; //스크롤 최상단 고정
					}
				})
			}
		}) */
	});
}


//////////////////////////			

/*$.ajax({
	url: "/cart/order",
	type: "POST",
	contentType: 'application/json',
	data: JSON.stringify(params),
	success: function(res) {
		console.log("res.result: ", res.result);
		if (res.result === 'succeed') {
			alert("여기까지됨");
			var IMP = window.IMP; 
			IMP.init('imp42653157');
			IMP.request_pay({
				pg: 'html5_inicis',
				pay_method: method,
				merchant_uid: res.orderNo,
				name: orderName,
				amount: amount,
				buyer_email: memberEmail,
				buyer_name: memberId,
				buyer_tel: memberPhone,
				buyer_addr: address,
				buyer_postcode: postalCode
			}, function(rsp) {
				if (rsp.success) {
					console.log("rsp.imp_uid: ", rsp.imp_uid);
						alert("결제 완료");
				  
					   //장바구니 목록에서 삭제
					$.ajax({
						url: '/cart/mycart/deleteCheck',
							type: 'post',
							contentType: 'application/json',
							data: JSON.stringify(arr),
							success: function(result) {
							console.log('장바구니에서 주문/결제 완료 상품 삭제 완료');
						},
						error : function(status, error){ console.log(status, error); }
					}).done(function() { location.href='/'; });
				  
										
										
				/*	$.ajax({
					  url: '/verify/' + rsp.imp_uid,
					  type: "POST",
					  success: function(data) {
						if(rsp.paid_amount == data.response.amount) {
						  alert("결제 완료");
						  
						  //장바구니 목록에서 삭제
							$.ajax({
								url: '/cart/mycart/deleteCheck',
									type: 'post',
									contentType: 'application/json',
									data: JSON.stringify(arr),
									success: function(result) {
									console.log('장바구니에서 주문/결제 완료 상품 삭제 완료');
								},
								error : function(status, error){ console.log(status, error); }
							}).done(function() { location.href='/'; });
						  
						} else {
						  alert("결제 검증 실패");
						   var msg = '결제에 실패하였습니다.';
								msg += '에러내용 : ' + rsp.error_msg;
								Swal.fire({
									icon: 'error',
									title: '잠시 후 다시 시도해 주세요',
									text: msg,
									confirmButtonColor: '#00008b',
									confirmButtonText: '확인'
								}).then((result) => {
									if(result.isConfirmed) {
										window.location.reload(); //페이지 새로고침
										window.history.scrollRestoration = 'manual'; //스크롤 최상단 고정
									}
								})
						}
					  },
					  error: function(err) {
						console.log("서버 검증 실패:", err);
					  }
					});
				*/

/*	} else {
		alert("결제 실패 : " + rsp.error_msg);
	}
});
	
}else{
alert("실패 ");
}
},
error: function(err) {
console.log("주문 생성 실패:", err);
}
});
} */
