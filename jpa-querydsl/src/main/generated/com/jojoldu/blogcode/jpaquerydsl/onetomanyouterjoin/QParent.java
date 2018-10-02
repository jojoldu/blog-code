package com.jojoldu.blogcode.jpaquerydsl.onetomanyouterjoin;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathInits;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QParent is a Querydsl query type for Parent
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QParent extends EntityPathBase<Parent> {

    private static final long serialVersionUID = -444126178L;

    public static final QParent parent = new QParent("parent");

    public final ListPath<Child, QChild> children = this.<Child, QChild>createList("children", Child.class, QChild.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QParent(String variable) {
        super(Parent.class, forVariable(variable));
    }

    public QParent(Path<? extends Parent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QParent(PathMetadata metadata) {
        super(Parent.class, metadata);
    }

}

