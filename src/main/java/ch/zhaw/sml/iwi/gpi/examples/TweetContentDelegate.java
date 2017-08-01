/*
 * Copyright (C) 2017 Peter Heinrich <heip@zhaw.ch>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.zhaw.sml.iwi.gpi.examples;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service("tweetAdapter")
public class TweetContentDelegate implements JavaDelegate {

    private static final Logger logger = Logger.getLogger(TweetContentDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String content = (String) execution.getVariable("content");
        logger.log(Level.INFO, "Twittering: {0}", content);
    }

}
