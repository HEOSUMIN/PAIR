/* ===상품등록=== */
const optionCountSelect = document.getElementById("optionCount");
const optionForm = document.getElementById("optionForm");
const optionInputs = document.getElementById("optionInputs");

/*옵션명, 옵션값 입력란 생성 */
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

/*옵션명, 옵션값 입력란 삭제시*/
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
document.getElementById('applyOptionBtn').addEventListener('click', () => {

	if (!validateOptionInputs()) { return; }

	const { optionNames, optionValues } = getOptionData();

	// thead 생성
	renderOptionTableHead(optionNames);

	// tbody 생성
	renderOptionTableBody(optionValues);

	// 테이블 표시
	document.getElementById('optionTable').style.display = 'table';
});

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
	obj.value = Number(obj.value.replace(/[^0-9]/g, '')).toLocaleString();
}

function optionOpen() {
	$('.optionForm').toggle();
}


/* 상품등록 폼 제출 */
function submitProductForm() {

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
	
	//옵션값 
	const { optionNames, optionValues } = getOptionData();
		formData.append("optionList", new Blob([JSON.stringify({ optionNames, optionValues })], { type: 'application/json' }));

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
/* ============ */


/* ===상품수정=== */

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
