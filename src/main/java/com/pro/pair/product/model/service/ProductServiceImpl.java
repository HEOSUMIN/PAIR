package com.pro.pair.product.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pro.pair.paging.model.dto.Criteria;
import com.pro.pair.product.model.dao.ProductMapper;
import com.pro.pair.product.model.dto.BrandDTO;
import com.pro.pair.product.model.dto.CategoryDTO;
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
	public int addOption(int prodNo, Map<String, Object> optionList) {
		int result =0;
		
		List<String> optionNames = (List<String>) optionList.get("optionNames");
		List<List<String>> optionValues = (List<List<String>>) optionList.get("optionValues");
		
		for (int i = 0; i < optionNames.size(); i++) {
			String optName = optionNames.get(i);

			OptionDTO option = new OptionDTO();
			option.setOptNameNm(optName);
			option.setProdNo(prodNo);
			option.setSortOrder(i);

			//옵션명 등록 
			productMapper.insertOptionName(option);

			List<String> optValues = optionValues.get(i);
			int optNameNo = option.getOptNameNo();

			 for (int j = 0; j < optValues.size(); j++) {
			        String optValueNm = optValues.get(j);
			        
			        OptionValueDTO optionValue = new OptionValueDTO();
			        optionValue.setOptNameNo(optNameNo);
			        optionValue.setOptValueNm(optValueNm);
			        optionValue.setSortOrder(j);  // 옵션값 정렬 순서

			        //옵션값 등록 
			        result = productMapper.insertOptionValue(optionValue);
			    }
		}
		return result;
	}
}
