package com.sist.web.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sist.web.domain.member.mapper.MemberMapper;
import com.sist.web.domain.member.vo.MemberVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
	private final MemberMapper mMapper;

	@Override
	public MemberVO memberInfoData(String username) {
		// TODO Auto-generated method stub
		return mMapper.memberInfoData(username);
	}

	@Override
	public int memberIdCheck(String username) {
		// TODO Auto-generated method stub
		return mMapper.memberIdCheck(username);
	}
	@Transactional
	@Override
	public int memberInsertData(MemberVO vo) {
		// TODO Auto-generated method stub
		int result = mMapper.memberInsertData(vo);
		if (result > 0) {
			mMapper.memberAuthInsert(vo.getUsername());
		}
				
		return result;
	}

	@Override
	public int memberAuthInsert(String username) {
		// TODO Auto-generated method stub
		return mMapper.memberAuthInsert(username);
	}
}
