require "logstash/filters/base"
require "logstash/namespace"
require "logstash/filters/nlp/stanford-nlp-wrapper.jar"
require "logstash/filters/nlp/stanford-corenlp-models-current.jar"
require "logstash/filters/nlp/stanford-corenlp-3.5.0.jar"
require "json"

class LogStash::Filters::NLP < LogStash::Filters::Base

  config_name "nlp"

  # New plugins should start life at milestone 1.
  milestone 1

  # Parse this value.
  config :source, :validate => :string, :default => "message"

  public
  def register
    # nothing to do
    java_import 'uk.co.jaywayco.Parser'
    @parser = Parser.new()
  end # def register

  public
  def filter(event)
    # return nothing unless there's an actual filter event
    return unless filter?(event)
    if @source
      if event[@source].to_s != ''
        # Replace the event message with our message as configured in the
        # config file.
        @result = @parser.processLine(event[@source])
        #event["message"] = event["message"] + @result.sentiment.to_s
        event["nlp.sentiment"] = @result.sentiment
        event["nlp.tokens"] = @result.tokens
        event["nlp.sentences"] = @result.sentences
      end
    end
    # filter_matched should go in the last line of our successful code
    filter_matched(event)
  end # def filter
end # class LogStash::Filters::NLP
