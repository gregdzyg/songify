package com.songify.song.infrastructure.controller;

import com.songify.song.domain.model.Song;
import com.songify.song.infrastructure.controller.dto.request.CreateSongRequestDto;
import com.songify.song.infrastructure.controller.dto.request.UpdateSongRequestDto;
import com.songify.song.infrastructure.controller.dto.response.*;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class SongMapper {

    public static GetSongResponseDto mapFromSongToGetSongResponseDto(Map<Integer, Song> database) {
        return new GetSongResponseDto(database);
    }

    public static SingleSongResponseDto mapFromSongToSingleSongResponseDto(Song song) {
        return new SingleSongResponseDto(song.name(), song.artist());
    }

    public static Song mapFromCreateSongRequestDtoToSong(CreateSongRequestDto requestDto) {
        return new Song(requestDto.name(), requestDto.artist());
    }

    public static CreateSongResponseDto mapFromSongToCreateSongResponseDto(Song song) {
        return new CreateSongResponseDto(song.name(), song.artist());
    }

    public static DeleteSongResponseDto mapFromSongToDeleteSongResponseDto(Integer id) {
        return new DeleteSongResponseDto("Deleted song with id: " + id, HttpStatus.OK);
    }

    public static Song mapFromUpdateSongRequestDtoToSong(UpdateSongRequestDto requestDto) {
        return new Song(requestDto.name(), requestDto.artist());
    }

    public static UpdateSongResponseDto mapFromSongToUpdateSongResponseDto(Song song) {
        return new UpdateSongResponseDto(song.name(), song.artist());
    }

    public static PartiallyUpdateSongResponseDto mapFromSongToPartiallyUpdateSongResponseDto(Song song) {
        return new PartiallyUpdateSongResponseDto(song.name(), song.artist());
    }

}
