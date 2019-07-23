package com.navastud.polls.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;

import com.navastud.polls.payload.PagedResponse;

public abstract class BaseConverter<T> {

	@Autowired
	@Qualifier("pagedConverter")
	protected PagedConverter<T> pagedConverter;

	public abstract PagedResponse<T> convertPageToPagedResponse(Page<?> pages);

	public PagedConverter<T> getPagedConverter() {
		return pagedConverter;
	}

}
