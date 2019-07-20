package com.navastud.polls.converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.navastud.polls.payload.PagedResponse;

@Component("pagedConverter")
public class PagedConverter<T> {

	@Autowired
	@Qualifier("pagedResponse")
	private PagedResponse<T> pagedResponse;

	public PagedResponse<T> convertPageToPagedResponse(Page<?> pages, List<T> content) {

		pagedResponse.setContent(content);
		pagedResponse.setLast(pages.isLast());
		pagedResponse.setPage(pages.getNumber());
		pagedResponse.setSize(pages.getSize());
		pagedResponse.setTotalElements(pages.getTotalElements());
		pagedResponse.setTotalPages(pages.getTotalPages());

		return pagedResponse;
	}

}
