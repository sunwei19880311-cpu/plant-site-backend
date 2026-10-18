package com.plantsite.cps.controller.publicapi;

import com.plantsite.common.R;
import com.plantsite.cps.entity.CpsProduct;
import com.plantsite.cps.service.CpsProductService;
import com.plantsite.cps.vo.CpsProductCardVO;
import com.plantsite.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * C 端公开接口：CPS 商品推荐（首页“养花的小帮手”等）
 */
@RestController
@RequestMapping("/api/cps/product")
@RequiredArgsConstructor
public class CpsPublicController {

    private final CpsProductService cpsProductService;
    private final S3Service s3Service;

    /** plantId 传则查该植物+全站通用，不传则仅全站通用；已上架 */
    @GetMapping("/list")
    public R<List<CpsProductCardVO>> list(@RequestParam(required = false) Long plantId,
                                          @RequestParam(defaultValue = "10") int limit) {
        List<CpsProductCardVO> list = cpsProductService.listOnShelf(plantId, limit).stream().map(p -> {
            CpsProductCardVO vo = new CpsProductCardVO();
            vo.setId(p.getId());
            vo.setTitle(p.getTitle());
            vo.setProductDesc(p.getProductDesc());
            vo.setPrice(formatPrice(p.getPrice()));
            vo.setImageUrl(s3Service.presignGetSafe(p.getObjectKey()));
            vo.setLinkUrl(p.getLinkUrl());
            vo.setPlatform(p.getPlatform());
            return vo;
        }).toList();
        return R.ok(list);
    }

    private String formatPrice(BigDecimal price) {
        return price == null ? null : "¥" + price.stripTrailingZeros().toPlainString();
    }
}
