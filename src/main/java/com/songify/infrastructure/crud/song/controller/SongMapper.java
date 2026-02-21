package com.songify.infrastructure.crud.song.controller;
import com.songify.domain.crud.dto.SongDto;
import com.songify.infrastructure.crud.song.controller.dto.request.CreateSongRequestDto;
import com.songify.infrastructure.crud.song.controller.dto.request.UpdateSongRequestDto;
import com.songify.infrastructure.crud.song.controller.dto.response.*;
import org.springframework.http.HttpStatus;

import java.util.List;


public class SongMapper {


    public static GetSongResponseDto mapFromSongDtoToGetSongResponseDto(List<SongDto> songs) {

        return new GetSongResponseDto(songs);
    }

    public static SingleSongResponseDto mapFromSongDtoToSingleSongResponseDto(SongDto song) {
        return new SingleSongResponseDto(song.id(), song.name());
    }

    public static CreateSongResponseDto mapFromSongDtoToCreateSongResponseDto(SongDto song) {
        return new CreateSongResponseDto(song.id(), song.name());
    }

    public static DeleteSongResponseDto mapFromSongToDeleteSongResponseDto(Long id) {
        return new DeleteSongResponseDto("Deleted song with id: " + id, HttpStatus.OK);
    }

    public static UpdateSongResponseDto mapFromSongDtoToUpdateSongResponseDto(SongDto song) {
        return new UpdateSongResponseDto(song.id(), song.name());
    }

    public static SongDto mapFromUpdateSongRequestDtoToSongDto(UpdateSongRequestDto requestDto) {
        return SongDto.builder().name(requestDto.name()).build();
    }

    public static SongDto mapFromCreateSongRequestDtoToSongDto(CreateSongRequestDto requestDto) {
        return SongDto.builder().name(requestDto.name()).build();
    }



}
