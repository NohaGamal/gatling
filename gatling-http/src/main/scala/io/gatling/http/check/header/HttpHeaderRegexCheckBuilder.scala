/**
 * Copyright 2011-2013 eBusiness Information, Groupe Excilys (www.ebusinessinformation.fr)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gatling.http.check.header

import io.gatling.core.check.extractor.regex.GroupExtractor
import io.gatling.core.session.{ Expression, Session }
import io.gatling.http.check.{ HttpCheckBuilders, HttpMultipleCheckBuilder }
import io.gatling.http.response.Response

object HttpHeaderRegexCheckBuilder {

	def headerRegex[X](headerName: Expression[String], pattern: Expression[String])(implicit groupExtractor: GroupExtractor[X]) = {

		val headerAndPattern = (session: Session) => for {
			headerName <- headerName(session)
			pattern <- pattern(session)
		} yield (headerName, pattern)

		new HttpMultipleCheckBuilder[Response, String](HttpCheckBuilders.headerCheckFactory, HttpCheckBuilders.passThroughResponsePreparer) {
			def findExtractor(occurrence: Int) = session => headerAndPattern(session).map(new SingleHttpHeaderRegexExtractor(_, occurrence))
			def findAllExtractor = session => headerAndPattern(session).map(new MultipleHttpHeaderRegexExtractor(_))
			def countExtractor = session => headerAndPattern(session).map(new CountHttpHeaderRegexExtractor(_))
		}
	}
}
