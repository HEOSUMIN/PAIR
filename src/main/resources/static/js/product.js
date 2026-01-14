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

//==============옵션관리=======================
/* 옵션 열기 클릭 시 */
function optionOpen(){
	if ($('.optionAdd').css('display') == 'none') {
		$('.optionAdd').show();  
		$('.optionDetailBtn').show();  
		$('.optionDetailDiv').show(); 
		
	} else {
		$('.optionAdd').hide();
		$('.optionDetailBtn').hide();  
		$('.optionDetailDiv').hide(); 
	}
}

/* 옵션 추가 버튼 클릭 시 */
let idNum = 0;
function optionAdd(){
	$('.optionAdd').append(
		'<div class="optionForm" id="optionForm'+ idNum +'">'+
			'<ul>'+
				'<li style="width:40%"><p>옵션명</p></li>'+
				'<li><p>옵션값 ( ,로 옵션값을 구분하여 입력해 주세요.)</p></li>'+
				'<li style="width:30%"></li>'+
				'<li style="width:10%"></li>'+
			'</ul>'+
			'<ul class="option111">'+
				'<li style="width:40%"><input type="text" class="optCategoryNm" name="optCategoryNm" id="optCategoryNm"></li>'+
				'<li><input type="text" name="optNm" id="optNm"></li>'+
				'<li style="width:30%"><input type="checkbox" id="combYn"><span style="color:#82888d;font-weight:400;">필수</span></li>'+
				'<li style="width:10%" id="optionDelBtn"><span>&times;</span></li>'+				
			'</ul>'+
		'</div>'
	)
	idNum++;
	
	$('.optionDetailBtn').show();  
}


/* 세부사항 입력 버튼 클릭 시 */
var combineYn = '';
function optionDetailBtn(){
	
	$('.optionTable').remove();
	
	var combineYn = document.getElementById('combineYn').value;
	console.log("combineYn::"+combineYn);
	
	/* 조합형 */  
	if(combineYn == "Y"){		//수정
	
		/* thead 구성 */
		var textThead="";
		for(var i=0; i< optCategory.length; i++){
			textThead += '<th>'+optCategory[i]+'</th>'
		}
				
		$('.optionDetailDiv').append(
			'<table class="optionTable" id="optionTable">'+
				'<thead>'+
			    	'<tr>'+
				      	'<th>checkBox</th>'+
						textThead+
						'<th>옵션추가금액</th>'+
				       	'<th>재고</th>'+
				        '<th>재고추가</th>'+
				        '<th>상태</th>'+
					'</tr>'+
				'</thead>'+
				'<tbody id="table_body">'+
				'</tbody>'+
			'</table>'	
		)
		
		var textTbody="";
		for(let i=0; i< optValue.length; i++){	//우선 옵션 2개까지만 가능하도록. 후에 수정 
			for(let j=0; j< optValue[i].length; j++){
				textTbody += '<tr>'+
								'<td>'+
									'<input type="checkbox" name="chkbox"  class="form_control">'+
								'</td>'+
								'<td>'+
									'<p>'+optValue[i][j]+'</p>'+
								'</td>'+
								'<td>'+
									'<p>'+optValue[i+1][j]+'</p>'+
								'</td>'+
								'<td>'+
									'<input type="text"  class="form_control">'+
								'</td>'+
								'<td>'+
									'<input type="text"   class="form_control">'+
								'</td>'+
								'<td>'+
									'<input type="text"   class="form_control">'+
								'</td>'+
								'<td>'+
									'<input type="text"   class="form_control">'+
								'</td>'+					
							'</tr>'
			}
		}
		/* tbody 구성 */
		$('#table_body').append(
			textTbody
		)
					
	}else{
	/* 비조합형 */
		let optCategory = new Array(); //옵션명 담을 배열 
		let optValue = new Array();		//옵션값 담을 배열
	
		for(var i=0; i< $('.option111').length ; i++){
			var optCategoryNm = $('.option111:eq('+i+') > li:eq(0)').children('#optCategoryNm').val();	//옵션명
			var optNm = $('.option111:eq('+i+') > li:eq(1)').children('#optNm').val()					//옵션값
			optCategory.push(optCategoryNm);
			optValue.push(optNm);
		}
	
	
		let optNmArry = new Array();
		var text="";
		for(var i=0; i< optCategory.length; i++){
			optNmArry = optValue[i].split(",");	// optValue배열에 담긴값 텍스트로 저장 
		
			for(var j=0; j<optNmArry.length; j++){
				text += '<tr>'+
							'<td>'+
								'<input type="checkbox" name="chkbox"  class="form_control">'+
							'</td>'+
							'<td>'+
								'<p>'+optCategory[i]+'</p>'+
							'</td>'+
							'<td>'+
								'<p>'+optNmArry[j]+'</p>'+
							'</td>'+
							'<td>'+
								'<input type="text" id="optionExtChrg"  class="form_control">'+
							'</td>'+
							'<td>'+
								'<input type="text" id="optionStock"  class="form_control">'+
							'</td>'+
							'<td>'+
								'<input type="text"   class="form_control">'+
							'</td>'+
							'<td>'+
								'<input type="text"   class="form_control">'+
							'</td>'+					
						'</tr>'
			}
		}
		
		$('.optionDetailDiv').show();  
		
		$('.optionDetailDiv').append(
			'<table class="optionTable" id="optionTable">'+
				'<thead>'+
			    	'<tr>'+
				      	'<th>checkBox</th>'+
						'<th>옵션명</th>'+
						'<th>옵션값</th>'+
						'<th>옵션추가금액</th>'+
				       	'<th>재고</th>'+
				        '<th>재고추가</th>'+
				        '<th>상태</th>'+
					'</tr>'+
				'</thead>'+
				'<tbody id="table_body">'+
				text+
				'</tbody>'+
			'</table>'
		)
	}
}

//==========================================

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
