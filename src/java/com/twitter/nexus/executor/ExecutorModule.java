package com.twitter.nexus.executor;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.internal.Preconditions;
import com.twitter.common.base.ExceptionalFunction;
import com.twitter.nexus.util.HdfsUtil;
import org.apache.hadoop.fs.FileSystem;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * ExecutorModule
 *
 * @author Florian Leibert
 */
public class ExecutorModule extends AbstractModule {
  private final static java.util.logging.Logger LOG = Logger.getLogger(ExecutorModule.class.getName());
  private final ExecutorMain.TwitterExecutorOptions options;

  @Inject
  public ExecutorModule(ExecutorMain.TwitterExecutorOptions options) {
    this.options = Preconditions.checkNotNull(options);
  }

  @Override
  protected void configure() {
    bind(ExecutorCore.class).in(Singleton.class);
    bind(ExecutorHub.class).in(Singleton.class);
  }

  @Provides
  @Singleton
  public FileSystem provideFileSystem() throws IOException {
    return HdfsUtil.getHdfsConfiguration(options.hdfsConfig);
  }

  @Provides
  public ExceptionalFunction<FileCopyRequest, File, IOException> provideFileCopier(
      final FileSystem fileSystem) {
    return new ExceptionalFunction<FileCopyRequest, File, IOException>() {
      @Override public File apply(FileCopyRequest copy) throws IOException {
        LOG.info(String.format(
            "HDFS file %s -> local file %s", copy.getSourcePath(), copy.getDestPath()));
        return HdfsUtil.downloadFileFromHdfs(fileSystem, copy.getSourcePath(), copy.getDestPath());
      }
    };
  }
}
