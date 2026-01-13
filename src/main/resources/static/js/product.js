/**
 * 상품등록, 
 */

let fileItems = document.querySelectorAll('[type=file]');
let thumbTd = document.querySelectorAll('.thumbTd');
let imageBox = document.querySelectorAll('.imageBox');
fileItems.forEach(item => item.addEventListener('change', previewImage));

/* 사진 파일 등록 */
function previewImage(){
	let index = Array.from(fileItems).indexOf(this); //fileItems 기준으로 index 생성
	console.log(index);
	if(this.files && this.files[0]) {
		let reader = new FileReader();
		reader.readAsDataURL(this.files[0]);
		reader.onload = function() {
			imageBox[index].innerHTML = "<img src='" + reader.result + "'>";
		}
	} else {
		thumbTd.forEach(item => item.addEventListener('click', reselectImage));
	}
}

function reselectImage() {
	let index = Array.from(thumbTd).indexOf(this); //thumbTd 기준으로 index 생성
	//console.log(index);
	if(index == 0) {
		fileItems[index].click();
	}
};

/*  대분류 변경 시  */
function changeCategory() {
	let categoryNo = document.getElementById('category').value;

	$('#subCategory').children('option').remove();
	$('#subCategory').prepend('<option selected disabled hidden >선택</option>');

	$.ajax({
		url: '/option',
		type: 'get',
		dataType: 'json',
		contentType: "application/json; charset=UTF-8",
		data: { categoryNo: categoryNo },
		success: function(result) {
			for (i = 0; i < result.length; i++) {
				console.log(result[i].categoryNm);
				$('#subCategory').append("<option value='" + result[i].categoryNo + "'>" + result[i].categoryNm + "</option>");
			}
		}
	}).fail(function(error) {
		alert(JSON.stringify(error));
	});
}


/* 금액입력시 (숫자, 콤마) */ 
function onlyNumberWithComma(obj) {
  obj.value = Number(obj.value.replace(/[^0-9]/g,'')).toLocaleString();
}


/* 상품등록 폼 제출 */
function submitProductForm(){
	//하위 카테고리 
	let subCategory = document.getElementById('subCategory').value;
	//카테고리 
	let category = document.getElementById('category').value;

	if(subCategory != "" || subCategory != null){
		category = document.getElementById('subCategory').value;	//최하위 카테고리로 들어가야함 
	}
	
	//브랜드
	let brand = document.getElementById('brand').value;
	
	//상품명 
	let prodNm = document.getElementById("prodNm").value;
	
	//상품 설명 
	let prodDesc = document.getElementById('prodDesc').value;
	
	//할인율
	let discountRate = document.getElementById('discount').value;
	
	//원가
	let prodPrice = document.getElementById('prodPrice').value;
	
	//상세내용
	let prodDetailContent = CKEDITOR.instances['prodDetailContent'].getData();
	console.log(prodDetailContent);
	
	//FormData 객체 생성
	let formData = new FormData();
	
	let attached = $('.files');
	console.log(attached.length);
	for(let i=0; i < attached.length; i++) {
		if(attached[i].files.length > 0) {
			for(let j=0; j < attached[i].files.length; j++) {
				formData.append("files", $('.files')[i].files[j]);
			}
		}
	}

	let params = { category : category
				 , brand : brand
				 , prodNm : prodNm
				 , prodDesc : prodDesc
				 , discountRate : discountRate
				 , prodPrice : prodPrice
				 , prodDetailContent : prodDetailContent
				 };
	
	formData.append("params", new Blob([JSON.stringify(params)], {type : 'application/json'}));
	
	
	for( let value of formData.values()){
		console.log(value);
	}
	
	$.ajax({
			url : '/admin/product/add',
			data : formData,
			processData: false,
			contentType: false,
			enctype: 'multipart/form-data',
			type : 'post',
			traditional : true,
			success : function(data){
				if(data.errorMessage) {
					Swal.fire({
						icon: 'error',
						title: data.errorMessage,
						confirmButtonColor: '#00008b',
						confirmButtonText: '확인'
					}).then((result) => {
						if(result.isConfirmed) {
							return;
						}
					})
				}
				
				if(data.successMessage) {
					Swal.fire({
						icon: 'success',
						title: data.successMessage,
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
			error : function(status, error){ console.log(status, error); }
	});
}


/* 상품수정 폼 제출 */
function submitEditProdForm(){
	//하위 카테고리 
	let subCategory = document.getElementById('subCategory').value;
	//카테고리 
	let category = document.getElementById('category').value;

	if(subCategory != "" || subCategory != null){
		category = document.getElementById('subCategory').value;	//최하위 카테고리로 들어가야함 
	}
	
	//브랜드
	let brand = document.getElementById('brand').value;
	
	//상품명 
	let prodNm = document.getElementById("prodNm").value;
	
	//상품 설명 
	let prodDesc = document.getElementById('prodDesc').value;
	
	//할인율
	let discountRate = document.getElementById('discount').value;
	
	//원가
	let prodPrice = document.getElementById('prodPrice').value;
	
	//상세내용
	let prodDetailContent = CKEDITOR.instances['prodDetailContent'].getData();
	console.log(prodDetailContent);
	
	//FormData 객체 생성
	let formData = new FormData();
	
	let attached = $('.files');
	console.log(attached.length);
	for(let i=0; i < attached.length; i++) {
		if(attached[i].files.length > 0) {
			for(let j=0; j < attached[i].files.length; j++) {
				formData.append("files", $('.files')[i].files[j]);
			}
		}
	}

	let params = { category : category
				 , brand : brand
				 , prodNm : prodNm
				 , prodDesc : prodDesc
				 , discountRate : discountRate
				 , prodPrice : prodPrice
				 , prodDetailContent : prodDetailContent
				 };
	
	formData.append("params", new Blob([JSON.stringify(params)], {type : 'application/json'}));
	
	
	for( let value of formData.values()){
		console.log(value);
	}
	
	$.ajax({
			url : '/admin/product/add',
			data : formData,
			processData: false,
			contentType: false,
			enctype: 'multipart/form-data',
			type : 'post',
			traditional : true,
			success : function(data){
				if(data.errorMessage) {
					Swal.fire({
						icon: 'error',
						title: data.errorMessage,
						confirmButtonColor: '#00008b',
						confirmButtonText: '확인'
					}).then((result) => {
						if(result.isConfirmed) {
							return;
						}
					})
				}
				
				if(data.successMessage) {
					Swal.fire({
						icon: 'success',
						title: data.successMessage,
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
			error : function(status, error){ console.log(status, error); }
	});
}
