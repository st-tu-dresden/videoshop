/*
 * Copyright 2013-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package videoshop.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

// (｡◕‿◕｡)
// Eigene Entity Klasse um Kommentare für Discs zu speichern
// Alle JPA Anforderungen erfüllt :)
// Mit der Table-Annotation kann man u.a. den Name der Tabelle angeben, ansonsten wird der Klassennamen genommen

@Entity
@Table(name = "COMMENTS")
public class Comment {

	// (｡◕‿◕｡)
	// Falls man die Id nicht selber setzen will, kann die mit @GeneratedValue vom JPA-Provider generiert und gesetzt
	// werden
	@Id @GeneratedValue private long id;

	private String text;
	private int rating;

	// (｡◕‿◕｡)
	// 1. Es gibt eine extra Annotation für Dates
	@Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime") private LocalDateTime date;

	@Deprecated
	protected Comment() {}

	public Comment(String text, int rating, LocalDateTime dateTime) {
		this.text = text;
		this.rating = rating;
		this.date = dateTime;
	}

	public String getText() {
		return text;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public int getRating() {
		return rating;
	}

	@Override
	public String toString() {
		return text;
	}
}
