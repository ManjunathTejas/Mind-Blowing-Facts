package com.amazonaws.lambda.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazonaws.services.lambda.runtime.Context;

public class DumbFactsSpeechlet implements Speechlet {
	
private static final Logger log = LoggerFactory.getLogger(DumbFactsSpeechlet.class);

private static final String[] DUMB_FACTS = new String[]{
    "The Harry Potter books get their name from the main character whose name is Harry Potter",
    "The page number in a book denotes the number of the page you're on ",
    "There are more people now than when there were fewer people ",
    "Statistics show women have a higher chance of getting pregnant compared to men",
    "In China the North Pole is referred to as East Antarctica",
    "The Sun is an almost perfect square",
    "There are more nipples in the world than people",
    "Every 60 seconds in Africa, a minute passed",
    "Happiness is like peeing your pants. Everyone can see it, but only you can feel the warmth"
};

@Override
	public void onSessionStarted(final SessionStartedRequest request, final Session session)
	        throws SpeechletException {
	    log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
	            session.getSessionId());
	    // any initialization logic goes here
	}

@Override
	public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
	        throws SpeechletException {
	    log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
	            session.getSessionId());
	    return getNewFactResponse();
	}

@Override
	public SpeechletResponse onIntent(final IntentRequest request, final Session session)
	        throws SpeechletException {
	    log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
	            session.getSessionId());
	
	    Intent intent = request.getIntent();
	    String intentName = (intent != null) ? intent.getName() : null;
	
	    if ("GetNewFactIntent".equals(intentName)) {
	        return getNewFactResponse();
	
	    } else if ("AMAZON.HelpIntent".equals(intentName)) {
	        return getHelpResponse();
	
	    } else if ("AMAZON.StopIntent".equals(intentName)) {
	        PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
	        outputSpeech.setText("Goodbye");
	
	        return SpeechletResponse.newTellResponse(outputSpeech);
	    } else if ("AMAZON.CancelIntent".equals(intentName)) {
	        PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
	        outputSpeech.setText("Goodbye");
	
	        return SpeechletResponse.newTellResponse(outputSpeech);
	    } else {
	        throw new SpeechletException("Invalid Intent");
	    }
	}

@Override
	public void onSessionEnded(final SessionEndedRequest request, final Session session)
	        throws SpeechletException {
	    log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
	            session.getSessionId());
	    // any cleanup logic goes here
	}

/**
 * Gets a random new fact from the list and returns to the user.
 */
	private SpeechletResponse getNewFactResponse() {
	    // Get a random space fact from the space facts list
	    int factIndex = (int) Math.floor(Math.random() * DUMB_FACTS.length);
	    String fact = DUMB_FACTS[factIndex];
	
	    // Create speech output
	    String speechText = "Did you know " + fact;
	
	    // Create the Simple card content.
	    SimpleCard card = new SimpleCard();
	    card.setTitle("DumbFacts");
	    card.setContent(speechText);
	
	    // Create the plain text output.
	    PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
	    speech.setText(speechText);
	
	    return SpeechletResponse.newTellResponse(speech, card);
	}
	
	/**
     * Returns a response for the help intent.
     */
    private SpeechletResponse getHelpResponse() {
        String speechText =
                "You can ask Mind Blowing Fact to blow my mind, or, you can say exit. What can I "
                        + "help you with?";

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt);
    }

}
