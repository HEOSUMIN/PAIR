package com.pro.pair.product.model.service;

import java.util.List;
import java.util.Map;

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
		int result =0;
		List<String> optionNames = (List<String>) optionData.get("optionNames");
		List<List<String>> optionValues = (List<List<String>>) optionData.get("optionValues");
		List<Map<String, Object>> optionCombinations =
		        (List<Map<String, Object>>) optionData.get("optionCombinations");
		
		//옵션명 등록
		for (int i=0; i<optionNames.size(); i++) {
			String optName = optionNames.get(i);
		
			OptionDTO option = new OptionDTO();
			option.setOptNameNm(optName); 	//옵션명이름
			option.setProdNo(prodNo);		//상품번호 
			option.setSortOrder(i);			//정렬순서
			//옵션명 테이블 등록 
			productMapper.insertOptionName(option);

			//옵션값 등록
			List<String> optValues = optionValues.get(i);
			int optNameNo = option.getOptNameNo();
			 for (int j=0; j<optValues.size(); j++) {
			        String optValueNm = optValues.get(j);
			        
			        OptionValueDTO optionValue = new OptionValueDTO();
			        optionValue.setOptNameNo(optNameNo);		//옵션명번호
			        optionValue.setOptValueNm(optValueNm);		//옵션값이름
			        optionValue.setSortOrder(j);				//정렬순서
			        //옵션값 테이블 등록 
			        result = productMapper.insertOptionValue(optionValue);
			    }
		}
		
		//옵션 조합 등록
		for(int i=0; i<optionCombinations.size(); i++) {
			Map<String, Object> comb = optionCombinations.get(i);
			int addPrice = Integer.parseInt(comb.get("optionPrice").toString());
		    int stockQty    = Integer.parseInt(comb.get("stockQty").toString());
		   // String saleStatus = comb.get("saleStatus").toString();
		    String manageCode = comb.get("manageCode").toString();
		    
			OptionCombDTO optComb = new OptionCombDTO();
			optComb.setProdNo(prodNo);
			optComb.setOptAddPrice(addPrice);
			optComb.setOptStockQty(stockQty);
			optComb.setOptManageNm(manageCode);
			//옵션값 테이블 등록 
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
}
