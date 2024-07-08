package com.onetuks.libraryexternal.book.handler.strategy.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record CollectionQueryResponse(
    @JsonProperty("total") String total,
    @JsonProperty("kwd") String kwd,
    @JsonProperty("pageNum") String pageNum,
    @JsonProperty("pageSize") String pageSize,
    @JsonProperty("category") String category,
    @JsonProperty("sort") String sort,
    @JsonProperty("result") List<CollectionQueryData> result) {

  @JsonCreator
  public CollectionQueryResponse {}

  public record CollectionQueryData(
      @JsonProperty("titleInfo") String titleInfo,
      @JsonProperty("typeName") String typeName,
      @JsonProperty("placeInfo") String placeInfo,
      @JsonProperty("authorInfo") String authorInfo,
      @JsonProperty("pubInfo") String pubInfo,
      @JsonProperty("menuName") String menuName,
      @JsonProperty("mediaName") String mediaName,
      @JsonProperty("manageName") String manageName,
      @JsonProperty("pubYearInfo") String pubYearInfo,
      @JsonProperty("controlNo") String controlNo,
      @JsonProperty("docYn") String docYn,
      @JsonProperty("orgLink") String orgLink,
      @JsonProperty("id") String id,
      @JsonProperty("typeCode") String typeCode,
      @JsonProperty("licYn") String licYn,
      @JsonProperty("licText") String licTest,
      @JsonProperty("regDate") String regDate,
      @JsonProperty("detailLink") String detailLink,
      @JsonProperty("isbn") String isbn,
      @JsonProperty("callNo") String callNo,
      @JsonProperty("kdcCode1s") String kdcCode1s,
      @JsonProperty("kdcName1s") String kdcName1s,
      @JsonProperty("classNo") String classNo,
      @JsonProperty("imageUrl") String imageUrl) {

    @JsonCreator
    public CollectionQueryData {}
  }
}
