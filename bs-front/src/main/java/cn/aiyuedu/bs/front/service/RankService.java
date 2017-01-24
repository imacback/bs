package cn.aiyuedu.bs.front.service;

import cn.aiyuedu.bs.front.vo.RankVo;

import java.util.List;

/**
 * Description:
 *
 * @author Scott
 */
//@Service
public abstract class RankService {

    public abstract List<RankVo> list(Integer platform);
}
