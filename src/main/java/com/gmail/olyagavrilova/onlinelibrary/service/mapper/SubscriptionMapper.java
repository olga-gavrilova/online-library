package com.gmail.olyagavrilova.onlinelibrary.service.mapper;

import com.gmail.olyagavrilova.onlinelibrary.dao.entity.Subscription;
import com.gmail.olyagavrilova.onlinelibrary.dto.SubscriptionDto;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SubscriptionMapper extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        ConverterFactory converterFactory = factory.getConverterFactory();
        converterFactory.registerConverter("localDateConverter", new PassThroughConverter(LocalDate.class));

        factory.classMap(Subscription.class, SubscriptionDto.class)
                .fieldAToB("book.title", "bookTitle")
                .fieldAToB("book.id", "bookId")
                .fieldAToB("book.author", "author")
                .fieldAToB("book.publisher", "publisher")
                .fieldMap("dateOfReturn", "dateOfReturn").converter("localDateConverter").add()
                .byDefault()
                .register();
    }
}
