package com.hei.exo.file.hash;

import com.hei.exo.PojaGenerated;

@PojaGenerated
public record FileHash(FileHashAlgorithm algorithm, String value) {}
