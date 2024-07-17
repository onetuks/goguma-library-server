package com.onetuks.librarydomain.weakly;

import java.time.LocalDateTime;

public record WeaklyFeaturedBooksEvent(
    Long weaklyFeaturedBooksEventId,
    LocalDateTime startedAt,
    LocalDateTime endedAt) {}
