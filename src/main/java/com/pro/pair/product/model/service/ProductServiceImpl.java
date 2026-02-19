package com.pro.pair.product.model.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pro.pair.paging.model.dto.Criteria;
import com.pro.pair.paging.model.dto.ItemCriteria;
import com.pro.pair.product.model.dao.ProductMapper;
import com.pro.pair.product.model.dto.BrandDTO;
import com.pro.pair.product.model.dto.CategoryDTO;
import com.pro.pair.product.model.dto.OptionCombDTO;
import com.pro.pair.product.model.dto.OptionDTO;
import com.pro.pair.product.model.dto.OptionValueDTO;
import com.pro.pair.product.model.dto.ProductDTO;
import com.pro.pair.review.model.dto.ReviewDTO;
import com.pro.pair.upload.model.dto.AttachmentDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("productService")
public class ProductServiceImpl implements ProductService {
	private ProductMapper productMapper;

	@Autowired
	public ProductServiceImpl(ProductMapper productMapper) {
		this.productMapper = productMapper;
	}

	@Override
	public List<BrandDTO> getBrandList() {
		// TODO Auto-generated method stub
		return productMapper.getBrandList();
	}

	@Override
	public List<CategoryDTO> getCategoryList() {
		return productMapper.getCategoryList();
	}

	@Override
	public List<CategoryDTO> getSubCategoryList(int categoryNo) {
		return productMapper.getSubCategoryList(categoryNo);
	}

	@Override
	public int checkCurrProdNo() {
		return productMapper.checkCurrProdNo();
	}

	@Override
	public int attachProdThumbnail(AttachmentDTO attachment) {
		return productMapper.attachProdThumbnail(attachment);
	}

	@Override
	public int addProduct(ProductDTO product) {
		int result = productMapper.addProduct(product);

		return result;
	}

	@Override
	public int getTotalNumber(Criteria criteria) {
		return productMapper.getTotalNumber(criteria);
	}
	
	@Override
	public int getTotalNumberByCriteria(ItemCriteria itemCriteria) {
		return productMapper.getTotalNumberByCriteria(itemCriteria);
	}

	@Override
	public int getOnSaleNumber(Criteria criteria) {
		return productMapper.getOnSaleNumber(criteria);
	}

	@Override
	public List<ProductDTO> getProductList(Criteria criteria) {
		return productMapper.getProductList(criteria);
	}

	@Override
	public List<ProductDTO> getOnSaleOnly(Criteria criteria) {
		return productMapper.getOnSaleOnly(criteria);
	}

	@Override
	public ProductDTO getProductDetails(int prodNo) {
		return productMapper.getProductDetails(prodNo);
	}

	@Override
	public AttachmentDTO getMainThumbnailByProdNo(int prodNo) {
		return productMapper.getMainThumbnailByProdNo(prodNo);
	}

	@Override
	public AttachmentDTO getSubThumbnailByProdNo(int prodNo) {
		return productMapper.getMainThumbnailByProdNo(prodNo);
	}

	@Override
	@Transactional
	public int addOption(int prodNo, Map<String, Object> optionData) {
		int result = 0;
		List<String> optionNames = (List<String>) optionData.get("optionNames");
		List<List<String>> optionValues = (List<List<String>>) optionData.get("optionValues");
		List<Map<String, Object>> optionCombinations = (List<Map<String, Object>>) optionData.get("optionCombinations");

		// 옵션명 등록
		for (int i = 0; i < optionNames.size(); i++) {
			String optName = optionNames.get(i);

			OptionDTO option = new OptionDTO();
			option.setOptNameNm(optName); // 옵션명이름
			option.setProdNo(prodNo); // 상품번호
			option.setSortOrder(i); // 정렬순서
			// 옵션명 테이블 등록
			productMapper.insertOptionName(option);

			// 옵션값 등록
			List<String> optValues = optionValues.get(i);
			int optNameNo = option.getOptNameNo();
			for (int j = 0; j < optValues.size(); j++) {
				String optValueNm = optValues.get(j);

				OptionValueDTO optionValue = new OptionValueDTO();
				optionValue.setOptNameNo(optNameNo); // 옵션명번호
				optionValue.setOptValueNm(optValueNm); // 옵션값이름
				optionValue.setSortOrder(j); // 정렬순서
				// 옵션값 테이블 등록
				result = productMapper.insertOptionValue(optionValue);
			}
		}

		// 옵션 조합 등록
		for (int i = 0; i < optionCombinations.size(); i++) {
			Map<String, Object> comb = optionCombinations.get(i);
			int addPrice = Integer.parseInt(comb.get("optionPrice").toString());
			int stockQty = Integer.parseInt(comb.get("stockQty").toString());
			// String saleStatus = comb.get("saleStatus").toString();
			String manageCode = comb.get("manageCode").toString();

			OptionCombDTO optComb = new OptionCombDTO();
			optComb.setProdNo(prodNo);
			optComb.setOptAddPrice(addPrice);
			optComb.setOptStockQty(stockQty);
			optComb.setOptManageNm(manageCode);
			// 옵션값 테이블 등록
			result = productMapper.insertOptionComb(optComb);
		}
		return result;
	}

	@Override
	public List<ProductDTO> getProductListByCategorySection(ItemCriteria itemCriteria) {
		return productMapper.getProductListByCategorySection(itemCriteria);
	}

	@Override
	public List<OptionDTO> getOptionListByProdNo(int prodNo) {
		return productMapper.getOptionListByProdNo(prodNo);
	}

	@Override
	public List<OptionCombDTO> getOptionDetailsListByProdNo(int prodNo) {
		return productMapper.getOptionDetailsListByProdNo(prodNo);
	}

	@Override
	public OptionCombDTO findOptionCombByName(int prodNo, String combName) {
		return productMapper.findOptionCombByName(prodNo, combName);
	}

	@Override
	public List<ReviewDTO> getReviewListByProdNo(int prodNo) {
		return productMapper.getReviewListByProdNo(prodNo);
	}

	@Override
	public List<ProductDTO> getTodayRecommendation(int limit) {

		// 1. 조합 2개 이상인 상품 전체 조회
		List<Integer> getProducts = productMapper.findProductsWithMultipleCombinations();

		log.info("조합 2개 이상인 상품 eligibleProducts:{}", getProducts);
		
		// 2. 랜덤 섞기
		Collections.shuffle(getProducts);

		// 3. 상위 3개 상품 선택
		List<Integer> randomProducts = getProducts.stream().limit(limit).collect(Collectors.toList());
		
		log.info("상위 3개 randomProducts:{}", randomProducts);

		// 4. 각 상품별 조합 2~3개 랜덤 선택
		List<ProductDTO> result = new ArrayList<>();
		for (Integer prodNo : randomProducts) {
			
			ProductDTO prodctDetails = productMapper.getProductDetailsWithThumbnail(prodNo);
			
			
			List<ProductDTO> combinations = productMapper.findCombinationsByProducNo(prodNo);
			log.info("combinations :{}", combinations);
			
			Collections.shuffle(combinations);
			
			List<ProductDTO> randomCombinations = combinations.stream().limit(limit).collect(Collectors.toList());
			log.info("randomCombinations :{}", randomCombinations);
			
			
			prodctDetails.setProdCombs(randomCombinations);
			
			result.add(prodctDetails);
			
		}
		
		log.info("result :{}", result);

		return result;
	}

	@Override
	public int getTotalNumberOfReviews(int prodNo) {
		return productMapper.getTotalNumberOfReviews(prodNo);
	}

	@Override
	public double averageReviewRating(int prodNo) {
		return productMapper.averageReviewRating(prodNo);
	}

}
