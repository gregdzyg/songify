package com.songify.infrastructure.crud.song.controller.dto.response;

import com.songify.domain.crud.dto.SongDto;

import java.util.List;

public record GetSongResponseDto(List<SongDto> songs) {
}
