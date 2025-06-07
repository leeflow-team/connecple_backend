package com.connecple.connecple_backend.domain.main.intro.repository;

import com.connecple.connecple_backend.domain.main.intro.entity.dto.MainIntroImageDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.connecple.connecple_backend.domain.main.intro.entity.QMainIntroImage.mainIntroImage;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MainIntroImageRepositoryImpl implements MainIntroImageRepositoryQdsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MainIntroImageDto> getMainIntroImages() {
        return queryFactory.select(
                Projections.constructor(MainIntroImageDto.class,
                        mainIntroImage
                        ))
                .from(mainIntroImage)
                .orderBy(mainIntroImage.sortOrder.asc())
                .fetch();
    }
}
