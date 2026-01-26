/* ================================관리자-상품등록================================ */
const optionCountSelect = document.getElementById("optionCount");
const optionForm = document.getElementById("optionForm");
const optionInputs = document.getElementById("optionInputs");
const applyOptionBtn = document.getElementById("applyOptionBtn");

/*옵션명, 옵션값 입력란 생성 */
if (optionCountSelect) { // 엘리먼트가 있으면
	optionCountSelect.addEventListener("change", function() {
		const count = parseInt(this.value);

		if (!count) {
			optionForm.style.display = "none";
			optionInputs.innerHTML = "";
			return;
		}

		// 폼 표시
		optionForm.style.display = "block";
		optionInputs.innerHTML = "";

		// 개수만큼 옵션 입력 row 생성
		for (let i = 0; i < count; i++) {
			const row = document.createElement("div");
			row.className = "option-row";
			row.innerHTML = `
      <input type="text"
             class="opt-name"
             name="optionList[${i}].optionName"
             placeholder="옵션명 (예: 컬러)">
      <input type="text"
             class="opt-values"
             name="optionList[${i}].optionValues"
             placeholder="옵션값 (예: 화이트,블랙)">
      <button type="button" class="btn-remove">×</button>
    `;
			optionInputs.appendChild(row);
		}
	});

}

/*옵션명, 옵션값 입력란 삭제시*/
if (optionInputs) { // 엘리먼트가 있으면
	optionInputs.addEventListener("click", function(e) {
		if (e.target.classList.contains('btn-remove')) {
			const row = e.target.closest('.option-row');
			row.remove();

			const rowCount = document.querySelectorAll(".option-row").length;

			// select 값 갱신
			optionCountSelect.value = rowCount;

			// 전부 삭제된 경우
			if (rowCount === 0) {
				optionForm.style.display = "none";
			}
		}
	});
}

/* 옵션 입력란 빈값 체크 */
function validateOptionInputs() {
	//옵션개수 존재하는 경우에만 
	if (!optionCountSelect.value) { return; }

	const rows = document.querySelectorAll('.option-row');

	for (let i = 0; i < rows.length; i++) {
		const nameInput = rows[i].querySelector('.opt-name');
		const valueInput = rows[i].querySelector('.opt-values');

		if (!nameInput.value.trim()) {
			alert(`${i + 1}번째 옵션명의 값을 입력해주세요.`);
			nameInput.focus();
			return false;
		}

		if (!valueInput.value.trim()) {
			alert(`${i + 1}번째 옵션값을 입력해주세요.`);
			valueInput.focus();
			return false;
		}
	}
	return true;
}

/* 옵션목록으로 적용 버튼 클릭시 */
if (applyOptionBtn) { // 엘리먼트가 있으면
	applyOptionBtn.addEventListener('click', () => {
		if (!validateOptionInputs()) { return; }
		const { optionNames, optionValues } = getOptionData();

		// thead 생성
		renderOptionTableHead(optionNames);
		// tbody 생성
		renderOptionTableBody(optionValues);
		// 테이블 표시
		document.getElementById('optionTable').style.display = 'table';
	});
}

/* 옵션데이터 */
function getOptionData() {
	const optionNames = [];
	const optionValues = [];

	// 옵션 row 전부 가져오기
	const rows = document.querySelectorAll('.option-row');

	rows.forEach(row => {
		const nameInput = row.querySelector('.opt-name');
		const valueInput = row.querySelector('.opt-values');

		const name = nameInput.value.trim();
		const values = valueInput.value.split(',');

		// 옵션값 정리 (공백 제거)
		const cleanValues = [];
		values.forEach(v => {
			if (v.trim() !== '') {
				cleanValues.push(v.trim());
			}
		});

		// 유효한 것만 추가
		if (name !== '' && cleanValues.length > 0) {
			optionNames.push(name);
			optionValues.push(cleanValues);
		}
	});

	return { optionNames, optionValues };
}

/* 옵션목록 클릭시 테이블 헤더 생성 */
function renderOptionTableHead(optionNames) {
	const thead = document.getElementById('optionTableHead');
	thead.innerHTML = '';

	const optionCount = optionNames.length;

	// 1행
	const tr1 = document.createElement('tr');
	tr1.innerHTML = `
    <th rowspan="2" class="chk-col"><input type="checkbox"></th>
    <th colspan="${optionCount}">옵션명</th>
    <th rowspan="2">옵션가</th>
    <th rowspan="2">재고수량</th>
    <th rowspan="2">판매상태</th>
    <th rowspan="2">관리코드</th>
    <th rowspan="2" class="useYn-col">사용여부</th>
    <th rowspan="2" class="del-col">삭제</th>
  `;

	// 2행
	const tr2 = document.createElement('tr');
	optionNames.forEach(name => {
		const th = document.createElement('th');
		th.textContent = name;
		tr2.appendChild(th);
	});

	thead.appendChild(tr1);
	thead.appendChild(tr2);
}

/* 옵션목록 클릭시 테이블 바디 생성 */
function renderOptionTableBody(optionValues) {
	const tbody = document.getElementById('optionTableBody');
	tbody.innerHTML = '';

	const combinations = getCombinations(optionValues);

	combinations.forEach((combo, index) => {
		const tr = document.createElement('tr');

		// 체크박스
		tr.innerHTML = `<td class="chk-col"><input type="checkbox"></td>`;

		// 옵션명 td들
		combo.forEach(value => {
			const td = document.createElement('td');
			td.textContent = value;
			tr.appendChild(td);
		});

		// 옵션가
		tr.innerHTML += `
      <td><input type="number" value="0"></td>
      <td><input type="number" value="0"></td>
      <td>판매중</td>
      <td>${combo.join('_')}</td>
      <td class="useYn-col">Y</td>
      <td class="del-col"><button type="button">×</button></td>
    `;

		tbody.appendChild(tr);
	});
}

/* 경우의수 조합 */
function getCombinations(arrays) {
	let result = [[]]; // 시작은 빈 조합

	for (let i = 0; i < arrays.length; i++) {
		const current = arrays[i];
		const temp = [];

		for (let j = 0; j < result.length; j++) {
			for (let k = 0; k < current.length; k++) {
				temp.push(result[j].concat(current[k]));
			}
		}

		result = temp;
	}

	return result;
}

let fileItems = document.querySelectorAll('[type=file]');
let thumbTd = document.querySelectorAll('.thumbTd');
let imageBox = document.querySelectorAll('.imageBox');
fileItems.forEach(item => item.addEventListener('change', previewImage));

/* 사진 파일 등록 */
function previewImage() {
	let index = Array.from(fileItems).indexOf(this); //fileItems 기준으로 index 생성
	console.log(index);
	if (this.files && this.files[0]) {
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
	if (index == 0) {
		fileItems[index].click();
	}
};

/*  대분류 변경 시  */
function changeCategory() {
	let categoryNo = document.getElementById('category').value;

	$('#subCategory').children('option').remove();
	$('#subCategory').prepend('<option selected disabled hidden value="" >선택</option>');

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
	obj.value = Number(obj.value.replace(/[^0-9]/g, '')).toLocaleString();
}

function optionOpen() {
	$('.optionForm').toggle();
}

/* 옵션테이블 정보 가져오기 */
function getOptionCombData() {
	const rows = document.querySelectorAll('#optionTableBody tr');
	const optionRows = [];

	rows.forEach(row => {
		const optionValues = [];
		// 옵션값들이 td 첫 번째 칸부터 optionNames.length 칸까지라고 가정
		// 예: 색상, 사이즈 순서대로
		const optionCount = document.querySelectorAll('#optionTableHead tr:nth-child(2) th').length;

		for (let i = 1; i <= optionCount; i++) {
			optionValues.push(row.children[i].textContent.trim());
		}

		const optionPrice = parseInt(row.children[optionCount + 1].querySelector('input').value);
		const stockQty = parseInt(row.children[optionCount + 2].querySelector('input').value);
		const saleStatus = row.children[optionCount + 3].textContent.trim();
		const manageCode = row.children[optionCount + 4].textContent.trim();
		const useYn = row.children[optionCount + 5].textContent.trim();

		optionRows.push({
			optionPrice,
			stockQty,
			saleStatus,
			manageCode,
			useYn
		});
	});

	return optionRows;
}


/* 상품등록 폼 제출 */
function submitProductForm() {

	//옵션개수
	let optionCount = document.getElementById('optionCount').value;

	//하위 카테고리 
	let subCategory = document.getElementById('subCategory').value;
	//카테고리 
	let category = document.getElementById('category').value;

	// 하위 카테고리가 "선택 안 됐을 때"
	if (!subCategory) {
		category = category;   // 상위 사용
	} else {
		category = subCategory; // 하위 사용
	}

	console.log("2category: ", category);
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
	for (let i = 0; i < attached.length; i++) {
		if (attached[i].files.length > 0) {
			for (let j = 0; j < attached[i].files.length; j++) {
				formData.append("files", $('.files')[i].files[j]);
			}
		}
	}

	let params = {
		optionCount: optionCount
		, category: category
		, brand: brand
		, prodNm: prodNm
		, prodDesc: prodDesc
		, discountRate: discountRate
		, prodPrice: prodPrice
		, prodDetailContent: prodDetailContent
	};

	formData.append("params", new Blob([JSON.stringify(params)], { type: 'application/json' }));

	const { optionNames, optionValues } = getOptionData();
	const optionCombData = getOptionCombData();

	const optionData = {
		optionNames: optionNames,           // ["색상", "사이즈"]
		optionValues: optionValues,         // [["화이트", "블랙"], ["l", "s"]]
		optionCombinations: optionCombData  // 테이블의 모든 행 데이터
	};
	formData.append("optionData", new Blob([JSON.stringify(optionData)], { type: 'application/json' }));


	//옵션데이터
	//const { optionNames, optionValues } = getOptionData();
	//formData.append("optionList", new Blob([JSON.stringify({ optionNames, optionValues })], { type: 'application/json' }));

	//옵션 테이블 데이터
	//const getOptionCombData = getOptionCombData();
	//formData.append("getOptionCombData", new Blob([JSON.stringify(getOptionCombData)], { type: "application/json" }));

	for (let value of formData.values()) {
		console.log(value);
	}

	$.ajax({
		url: '/admin/product/add',
		data: formData,
		processData: false,
		contentType: false,
		enctype: 'multipart/form-data',
		type: 'post',
		traditional: true,
		success: function(data) {
			if (data.errorMessage) {
				Swal.fire({
					icon: 'error',
					title: data.errorMessage,
					confirmButtonColor: '#00008b',
					confirmButtonText: '확인'
				}).then((result) => {
					if (result.isConfirmed) {
						return;
					}
				})
			}

			if (data.successMessage) {
				Swal.fire({
					icon: 'success',
					title: data.successMessage,
					confirmButtonColor: '#00008b',
					confirmButtonText: '확인'
				}).then((result) => {
					if (result.isConfirmed) {
						window.location.reload(); //페이지 새로고침
						window.history.scrollRestoration = 'manual'; //스크롤 최상단 고정
					}
				})
			}
		},
		error: function(status, error) { console.log(status, error); }
	});
}
/* =========================================================================== */

/* ================================관리자-상품수정================================ */

/* 상품수정 폼 제출 */
function submitEditProdForm() {
	//하위 카테고리 
	let subCategory = document.getElementById('subCategory').value;
	//카테고리 
	let category = document.getElementById('category').value;

	if (subCategory != "" || subCategory != null) {
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
	for (let i = 0; i < attached.length; i++) {
		if (attached[i].files.length > 0) {
			for (let j = 0; j < attached[i].files.length; j++) {
				formData.append("files", $('.files')[i].files[j]);
			}
		}
	}

	let params = {
		category: category
		, brand: brand
		, prodNm: prodNm
		, prodDesc: prodDesc
		, discountRate: discountRate
		, prodPrice: prodPrice
		, prodDetailContent: prodDetailContent
	};

	formData.append("params", new Blob([JSON.stringify(params)], { type: 'application/json' }));


	for (let value of formData.values()) {
		console.log(value);
	}

	$.ajax({
		url: '/admin/product/add',
		data: formData,
		processData: false,
		contentType: false,
		enctype: 'multipart/form-data',
		type: 'post',
		traditional: true,
		success: function(data) {
			if (data.errorMessage) {
				Swal.fire({
					icon: 'error',
					title: data.errorMessage,
					confirmButtonColor: '#00008b',
					confirmButtonText: '확인'
				}).then((result) => {
					if (result.isConfirmed) {
						return;
					}
				})
			}

			if (data.successMessage) {
				Swal.fire({
					icon: 'success',
					title: data.successMessage,
					confirmButtonColor: '#00008b',
					confirmButtonText: '확인'
				}).then((result) => {
					if (result.isConfirmed) {
						window.location.reload(); //페이지 새로고침
						window.history.scrollRestoration = 'manual'; //스크롤 최상단 고정
					}
				})
			}
		},
		error: function(status, error) { console.log(status, error); }
	});
}
/* =========================================================================== */

/* ================================상품상세페이지================================= */

let preSelectOptions = new Array();	// 이전에 담은 옵션
let selectOptions = new Array();	// 현재 담은 옵션

/* 옵션 선택시 */
function handleOptionChange(current) {
	let selects = document.getElementsByClassName("selector");
	let lastSelect = selects[selects.length - 1];

	//이전의 옵션값 선택 안한 경우 
	const currentIndex = Array.from(selects).indexOf(current);
	for (let i = 0; i < currentIndex; i++) {
		if (selects[i].selectedIndex === 0) { // placeholder 선택 상태
			alert("먼저 이전 옵션을 선택해주세요!");
			current.selectedIndex = 0; // 선택 초기화
			return; // 함수 종료
		}
	}

	//선택한 옵션 값 넣기
	selectOptions = [];
	for (let i = 0; i < selects.length; i++) {
		if (selects[i].selectedIndex > 0) {
			selectOptions.push(
				selects[i].options[selects[i].selectedIndex].text
			);
		}
	}

	//마지막 select 선택시 
	if (current === lastSelect) {
		console.log("마지막 선택시 ");
		let prodText = selectOptions.join(" + "); // "화이트 + 블랙 + 레드" 형태

		// 이미 선택된 옵션인지 체크
		if (preSelectOptions.includes(prodText)) {
			alert("이미 선택한 옵션입니다!");

			for (let i = 0; i < selects.length; i++) {
				selects[i].selectedIndex = 0; // 첫 번째 option이 placeholder이므로 초기값
			}

			selectOptions = []; // 초기화

			return;
		} else {
			// 선택되지 않은 새로운 옵션이면 추가

			preSelectOptions.push(prodText);

			const row = document.createElement("div");
			row.innerHTML = '<div class="selectedInfo">' +
				'<div class="selectedName"></div>' +
				'<div class="countBox">' +
				'<button type="button" class="button-down" disabled><i class="fa-solid fa-minus"></i></button>' +
				'<input type="number" class="selectedAmount" name="selectedAmount" value="1">' +
				'<button type="button" class="button-up"><i class="fa-solid fa-plus"></i></button>' +
				'</div>' +
				'<div class="selectedPrice"></div>' +
				'<div class="selectedAddPrice" ></div>' +
				'<a href="#" class="button-delete" onclick="reset(this); return false;"><i class="fa-solid fa-xmark"></i></a>' +
				'</div>';
			document.getElementById("selectedOption").appendChild(row);

			let combinationStr = selectOptions.join(" + ");
			let dbFormat = combinationStr.replace(/\s*\+\s*/g, "_");

			let prodNo = document.getElementById("prodNo").value;
			console.log("prodNo: ", prodNo);

			fetch(`/product/option-combination?prodNo=${prodNo}&combName=${encodeURIComponent(dbFormat)}`)
				.then(res => res.json())
				.then(data => {
					if (data) {
						
						let basePrice = parseInt($('.getHiddenPrice').attr('value')); //원래금액 
						let addPrice = parseInt(data.optAddPrice); 	//추가금액 
						let totalPrice = basePrice + addPrice; 
						let combinationName = prodText; // "블랙 + M"
						
						$(row).find('.selectedInfo').data('totalPrice', totalPrice);	//상품+옵션 추가금액
						
						$(row).find('.selectedPrice').text(totalPrice.toLocaleString() + "원");
						$(row).find('.selectedPrice').attr('value', totalPrice);
						
						$(row).find('.selectedName').text("- " + combinationName);
						$(row).find('.selectedName').attr('value', combinationName);
						
						sumTotalPrice();

					} else {
						alert("해당 옵션 조합은 존재하지 않습니다.");
					}
				});

			for (let i = 0; i < selects.length; i++) {
				selects[i].selectedIndex = 0; // 첫 번째 option이 placeholder이므로 초기값
			}
			//다음선택을 위한 초기화
			selectOptions = [];
		}
	}
}

/* 수량 증가 */
$(document).on('click', '.countBox .button-up', function() { //up 버튼
	let selectedAmount = $(this).closest('div.selectedInfo').find('input[name=selectedAmount]').val(); //input
	//console.log("selectedAmount : " + selectedAmount);
	
	let count = parseInt(selectedAmount);
	count++;
	
	if (count > 1) {
		$(this).closest('div.selectedInfo').find('.button-down').prop('disabled', false);
	}
	$(this).closest('div.selectedInfo').find('input[name=selectedAmount]').val(count); //증가한 수량 대입

	//수량에 따른 판매가 계산
	let originalPrice = $(this).closest('.selectedInfo').data('totalPrice');
	let price = parseInt(originalPrice);
	let result = count * price;
	$(this).closest('div.selectedInfo').find('.selectedPrice').attr('value', result);
	$(this).closest('div.selectedInfo').find('.selectedPrice').text(result.toLocaleString('ko-KR') + "원"); //원화 단위로 출력
	sumTotalPrice();
});

/* 수량 감소 */
$(document).on('click', '.countBox .button-down', function() { //down 버튼
	let selectedAmount = $(this).closest('div.selectedInfo').find('input[name=selectedAmount]').val(); //input
	//console.log("selectedAmount : " + selectedAmount);
	let count = parseInt(selectedAmount);
	count--;
	console.log(count);
	if (count == 1) {
		$(this).closest('div.selectedInfo').find('.button-down').prop('disabled', true);
	}
	$(this).closest('div.selectedInfo').find('input[name=selectedAmount]').val(count); //감소한 수량 대입

	//수량에 따른 판매가 계산
	let originalPrice = $(this).closest('.selectedInfo').data('totalPrice');
	let price = parseInt(originalPrice);
	let result = count * price;
	$(this).closest('div.selectedInfo').find('.selectedPrice').attr('value', result);
	$(this).closest('div.selectedInfo').find('.selectedPrice').text(result.toLocaleString('ko-KR') + "원"); //원화 단위로 출력
	sumTotalPrice();
});

/* 수량 입력 */
$(document).on('change', 'input[name=selectedAmount]', function() { //input 값 변경
	let selectedAmount = $(this).closest('div.selectedInfo').find('input[name=selectedAmount]').val(); //input
	let count = parseInt(selectedAmount);
	if (count > 1) {
		$(this).closest('div.selectedInfo').find('.button-down').prop('disabled', false);
	} else if (count == 1) {
		$(this).closest('div.selectedInfo').find('.button-down').prop('disabled', true);
	}

	//수량에 따른 판매가 계산
	let originalPrice = $(this).closest('.selectedInfo').data('totalPrice');
	let price = parseInt(originalPrice);
	let result = count * price;
	$(this).closest('div.selectedInfo').find('.selectedPrice').attr('value', result);
	$(this).closest('div.selectedInfo').find('.selectedPrice').text(result.toLocaleString('ko-KR') + "원"); //원화 단위로 출력

	sumTotalPrice();
});

function sumTotalPrice() {
	let total = 0;
	document.querySelectorAll('.selectedPrice').forEach(function(item) {
		let price = parseInt(item.getAttribute('value'));
		total += price;
		console.log(item.getAttribute('value'));
		console.log(total);
	});
	console.log("총금액 : ", total);
	$('#totalPrice').text(total.toLocaleString('ko-KR'));
}

function reset(btn) {
  const row = btn.closest('.selectedInfo');
  const name = row.querySelector('.selectedName').getAttribute('value');
  
  preSelectOptions = preSelectOptions.filter(v => v !== name);

  row.remove();

  sumTotalPrice();
}

/* ============================================================================ */

















