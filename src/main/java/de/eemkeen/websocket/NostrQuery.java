package de.eemkeen.websocket;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class NostrQuery {
    List<String> ids;
    List<String> authors;
    List<Integer> kinds;
    @JsonProperty("#e")
    List<String> eTagIds;
    @JsonProperty("#p")
    List<String> pTagIds;
    Long since;
    Long until;
    Integer limit;
}

