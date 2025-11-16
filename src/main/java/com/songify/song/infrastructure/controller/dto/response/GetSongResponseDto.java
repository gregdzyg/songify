package com.songify.song.infrastructure.controller.dto.response;
import com.songify.song.domain.model.Song;

import java.util.List;
import java.util.Map;

public record GetSongResponseDto(List<SongDto> songs) {
}
