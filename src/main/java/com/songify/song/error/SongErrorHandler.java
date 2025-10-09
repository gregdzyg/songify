package com.songify.song.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@Log4j2
@RestControllerAdvice
public class SongErrorHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(SongNotFoundException.class)
    public ErrorSongResponseDto handleException(SongNotFoundException e) {
        log.warn("Error while accessing song");
        return new ErrorSongResponseDto(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
