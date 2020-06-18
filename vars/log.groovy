def info(message) {
    echo "INFO: ${message}"
}

def warning(message) {
    echo "WARNING: ${message}"
}

/**
调用
steps {
  log.info 'Starting'
  log.warning 'Nothing to do!'
}
*/